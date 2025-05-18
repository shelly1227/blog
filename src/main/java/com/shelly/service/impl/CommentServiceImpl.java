package com.shelly.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shelly.constants.CommonConstant;
import com.shelly.constants.MqConstant;
import com.shelly.constants.RedisConstant;

import com.shelly.entity.dto.MailDTO;
import com.shelly.entity.pojo.*;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.query.CommentQuery;
import com.shelly.entity.vo.req.CheckReq;
import com.shelly.entity.vo.req.CommentReq;
import com.shelly.entity.vo.res.*;
import com.shelly.enums.CommentTypeEnum;
import com.shelly.mapper.ArticleMapper;
import com.shelly.mapper.TalkMapper;
import com.shelly.mapper.UserMapper;
import com.shelly.service.CommentService;
import com.shelly.mapper.CommentMapper;
import com.shelly.service.EmailService;
import com.shelly.service.RedisService;
import com.shelly.service.SiteConfigService;
import com.shelly.utils.HTMLUtils;
import com.shelly.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
* @author Shelly6
* @description 针对表【t_comment】的数据库操作Service实现
* @createDate 2024-07-22 20:24:46
*/
@Service
@RequiredArgsConstructor
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{
    private final CommentMapper commentMapper;
    private final RedisService redisService;
    private final ArticleMapper articleMapper;
    private final TalkMapper talkMapper;
    private final UserMapper userMapper;
    private final SiteConfigService siteConfigService;
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private final EmailService emailService;
    private final RabbitTemplate rabbitTemplate;

    @Value("${blog.url}")
    private String websiteUrl;


    @Override
    public PageResult<CommentBackResp> listCommentBackVO(CommentQuery commentQuery) {
        // 查询后台评论数量
        Long count = commentMapper.countComment(commentQuery);
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询后台评论集合
        List<CommentBackResp> commentBackRespList = commentMapper.selectBackCommentList(commentQuery);
        return new PageResult<>(commentBackRespList, count);
    }

    @Override
    public PageResult<CommentResp> listCommentVO(CommentQuery commentQuery) {
        // 查询父评论数量
        Long count = commentMapper.selectCount(new LambdaQueryWrapper<Comment>()
                //友链为空，其他的为说说或者文章id
                .eq(Objects.nonNull(commentQuery.getTypeId()), Comment::getTypeId, commentQuery.getTypeId())
                .eq(Comment::getCommentType, commentQuery.getCommentType())
                .eq(Comment::getIsCheck, CommonConstant.TRUE)
                .isNull(Comment::getParentId));
        if (count == 0) {
            return new PageResult<>();
        }
        // 分页查询最父级评论
        List<CommentResp> commentRespList = commentMapper.selectParentComment(commentQuery);
        if (CollectionUtils.isEmpty(commentRespList)) {
            return new PageResult<>();
        }
        // 评论点赞
        Map<String, Integer> likeCountMap = redisService.getHashAll(RedisConstant.COMMENT_LIKE_COUNT);
        // 父评论id集合
        List<Integer> parentCommentIdList = commentRespList.stream().map(CommentResp::getId).toList();
        // 分组查询每组父评论下的子评论前三条，注意，这里是先全部混装在一起，再根据parentId分组
        List<ReplyResp> replyRespList = commentMapper.selectReplyByParentIdList(parentCommentIdList);
        // 封装子评论点赞量
        replyRespList.forEach(item -> item.setLikeCount(Optional.ofNullable(likeCountMap.get(item.getId().toString())).orElse(0)));
        // 根据父评论id生成对应子评论的Map，key为父评论id，value为子评论，但是缺少父评论回复数量
        Map<Integer, List<ReplyResp>> replyMap = replyRespList.stream().collect(Collectors.groupingBy(ReplyResp::getParentId));
        // 父评论的回复数量，一个简单查询，将父评论id作为id，变相实现了内连，依然是混装
        List<ReplyCountResp> replyCountList = commentMapper.selectReplyCountByParentId(parentCommentIdList);
        // 转换Map，key为父评论id，value为回复数量
        Map<Integer, Integer> replyCountMap = replyCountList.stream().collect(Collectors.toMap(ReplyCountResp::getCommentId, ReplyCountResp::getReplyCount));
        // 封装评论数据
        commentRespList.forEach(item -> {
            item.setLikeCount(Optional.ofNullable(likeCountMap.get(item.getId().toString())).orElse(0));
            item.setReplyVOList(replyMap.get(item.getId()));
            item.setReplyCount(Optional.ofNullable(replyCountMap.get(item.getId())).orElse(0));// 回复数量
        });
        return new PageResult<>(commentRespList, count);
    }

    @Override
    public List<RecentCommentResp> listRecentCommentVO() {
        return commentMapper.selectRecentComment();
    }

    @Override
    public List<ReplyResp> listReply(Integer commentId) {
        // 分页查询子评论
        List<ReplyResp> replyRespList = commentMapper.selectReplyByParentId(PageUtils.getLimit(), PageUtils.getSize(), commentId);
        // 子评论点赞Map
        Map<String, Integer> likeCountMap = redisService.getHashAll(RedisConstant.COMMENT_LIKE_COUNT);
        replyRespList.forEach(item -> item.setLikeCount(likeCountMap.get(item.getId().toString())));
        return replyRespList;
    }

    @Override
    @Transactional
    public void updateCommentCheck(CheckReq check) {
        List<Comment> commentList = check.getIdList().stream().map(id -> Comment.builder().id(id).isCheck(check.getIsCheck()).build()).collect(Collectors.toList());
        this.updateBatchById(commentList);
    }

    @Override
    public void addComment(CommentReq comment) {
        // 校验评论参数
        verifyComment(comment);
        SiteConfig siteConfig = siteConfigService.getSiteConfig();
        Integer commentCheck = siteConfig.getCommentCheck();
        // 过滤标签
        comment.setCommentContent(HTMLUtils.filter(comment.getCommentContent()));
        Comment newComment = Comment.builder()
                .fromUid(StpUtil.getLoginIdAsInt())
                .toUid(comment.getToUid())
                .typeId(comment.getTypeId())
                .commentType(comment.getCommentType())
                .parentId(comment.getParentId())
                .replyId(comment.getReplyId())
                .commentContent(comment.getCommentContent())
                .isCheck(commentCheck.equals(CommonConstant.FALSE) ? CommonConstant.TRUE : CommonConstant.FALSE)
                .build();
        // 保存评论
        commentMapper.insert(newComment);
        // 查询评论用户昵称
        String fromNickname = userMapper.selectOne(new LambdaQueryWrapper<User>()
                        .select(User::getNickname)
                        .eq(User::getId, StpUtil.getLoginIdAsInt()))
                .getNickname();
        // 通知用户,如果要通知的话，100%实时推送，需要使用websocket
        if (siteConfig.getEmailNotice().equals(CommonConstant.TRUE)) {
            CompletableFuture.runAsync(() -> notice(newComment, fromNickname), threadPoolTaskExecutor);
        }
    }
    private void notice(Comment comment, String fromNickname) {
        // 自己回复自己不用提醒
        if (comment.getFromUid().equals(comment.getToUid())) {
            return;
        }
        // 邮件标题
        String title = "友链";
        // 回复用户id
        Integer toUid = CommonConstant.BLOGGER_ID;//1
        // 父评论
        if (Objects.isNull(comment.getParentId())) {
            if (comment.getCommentType().equals(CommentTypeEnum.ARTICLE.getType())) {
                Article article = articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                        .select(Article::getArticleTitle, Article::getUserId)
                        .eq(Article::getId, comment.getTypeId()));
                title = article.getArticleTitle();
                toUid = article.getUserId();
            }
            if (comment.getCommentType().equals(CommentTypeEnum.TALK.getType())) {
                title = "说说";
                toUid = talkMapper.selectOne(new LambdaQueryWrapper<Talk>()
                                .select(Talk::getUserId)
                                .eq(Talk::getId, comment.getTypeId()))
                        .getUserId();
            }
            // 自己评论自己的作品，不用提醒
            if (comment.getFromUid().equals(toUid)) {
                return;
            }
        } else {
            // 子评论
            toUid = comment.getToUid();
            if (comment.getCommentType().equals(CommentTypeEnum.ARTICLE.getType())) {
                title = articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                                .select(Article::getArticleTitle)
                                .eq(Article::getId, comment.getTypeId()))
                        .getArticleTitle();
            }
            if (comment.getCommentType().equals(CommentTypeEnum.TALK.getType())) {
                title = "说说";
            }
        }
        // 查询回复用户邮箱、昵称、id
        User toUser = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getEmail, User::getNickname)
                .eq(User::getId, toUid));
        // 邮箱不为空
        if (StringUtils.hasText(toUser.getEmail())) {
            sendEmail(comment, toUser, title, fromNickname);
        }
    }
    private void verifyComment(CommentReq comment) {
        if (comment.getCommentType().equals(CommentTypeEnum.ARTICLE.getType())) {
            Article article = articleMapper.selectOne(new LambdaQueryWrapper<Article>().select(Article::getId).eq(Article::getId, comment.getTypeId()));
            Assert.notNull(article, "文章不存在");
        }
        if (comment.getCommentType().equals(CommentTypeEnum.TALK.getType())) {
            Talk talk = talkMapper.selectOne(new LambdaQueryWrapper<Talk>().select(Talk::getId).eq(Talk::getId, comment.getTypeId()));
            Assert.notNull(talk, "说说不存在");
        }
        // 评论为子评论，判断回复的评论和用户是否存在
        Optional.ofNullable(comment.getParentId()).ifPresent(parentId -> {
            // 判断父评论是否存在
            Comment parentComment = commentMapper.selectOne(new LambdaQueryWrapper<Comment>().select(Comment::getId, Comment::getParentId, Comment::getCommentType).eq(Comment::getId, parentId));
            Assert.notNull(parentComment, "父评论不存在");
            //这个当前评论说的是回复的那个评论
            Assert.isNull(parentComment.getParentId(), "当前评论为子评论，不能作为父评论");
            Assert.isTrue(comment.getCommentType().equals(parentComment.getCommentType()), "只能以同类型的评论作为父评论");
            // 判断回复的评论和用户是否存在
            Comment replyComment = commentMapper.selectOne(new LambdaQueryWrapper<Comment>().select(Comment::getId, Comment::getParentId, Comment::getFromUid, Comment::getCommentType).eq(Comment::getId, comment.getReplyId()));
            User toUser = userMapper.selectOne(new LambdaQueryWrapper<User>().select(User::getId).eq(User::getId, comment.getToUid()));
            Assert.notNull(replyComment, "回复的评论不存在");
            Assert.notNull(toUser, "回复的用户不存在");
            Assert.isTrue(comment.getCommentType().equals(replyComment.getCommentType()), "只能回复同类型的下的评论");
            if (Objects.nonNull(replyComment.getParentId())) {
                Assert.isTrue(replyComment.getParentId().equals(parentId), "提交的评论parentId与当前回复评论parentId不一致");
            }
            Assert.isTrue(replyComment.getFromUid().equals(comment.getToUid()), "提交的评论toUid与当前回复评论fromUid不一致");
            // 只能回复当前父评论及其子评论
            List<Integer> replyIdList = commentMapper.selectCommentIdByParentId(parentId);
            replyIdList.add(parentId);
            Assert.isTrue(replyIdList.contains(comment.getReplyId()), "当前父评论下不存在该子评论");
        });
    }
    private void sendEmail(Comment comment, User toUser, String title, String fromNickname) {
        MailDTO mailDTO = new MailDTO();
        if (comment.getIsCheck().equals(CommonConstant.TRUE)) {
            Map<String, Object> contentMap = new HashMap<>(7);
            // 评论链接
            String typeId = Optional.ofNullable(comment.getTypeId())
                    .map(Object::toString)
                    .orElse("");
            String url = websiteUrl + CommentTypeEnum.getCommentPath(comment.getCommentType()) + typeId;
            // 父评论则提醒作者
            if (Objects.isNull(comment.getParentId())) {
                mailDTO.setToEmail(toUser.getEmail());
                mailDTO.setSubject(CommonConstant.COMMENT_REMIND);
                mailDTO.setTemplate(CommonConstant.AUTHOR_TEMPLATE);
                String createTime = DateUtil.formatLocalDateTime(comment.getCreateTime());
                contentMap.put("time", createTime);
                contentMap.put("url", url);
                contentMap.put("title", title);
                contentMap.put("nickname", fromNickname);
                contentMap.put("content", comment.getCommentContent());
                mailDTO.setContentMap(contentMap);
            } else {
                // 子评论则回复的是用户提醒该用户
                Comment parentComment = commentMapper.selectOne(new LambdaQueryWrapper<Comment>()
                        .select(Comment::getCommentContent, Comment::getCreateTime)
                        .eq(Comment::getId, comment.getReplyId()));
                mailDTO.setToEmail(toUser.getEmail());
                mailDTO.setSubject(CommonConstant.COMMENT_REMIND);
                mailDTO.setTemplate(CommonConstant.USER_TEMPLATE);
                contentMap.put("url", url);
                contentMap.put("title", title);
                String createTime = DateUtil.formatLocalDateTime(comment.getCreateTime());
                contentMap.put("time", createTime);
                // 被回复用户昵称
                contentMap.put("toUser", toUser.getNickname());
                // 评论用户昵称
                contentMap.put("fromUser", fromNickname);
                // 被回复的评论内容
                contentMap.put("parentComment", parentComment.getCommentContent());
                // 回复评论内容
                contentMap.put("replyComment", comment.getCommentContent());
                mailDTO.setContentMap(contentMap);
            }
            // 发送HTML邮件
            rabbitTemplate.convertAndSend(MqConstant.EMAIL_EXCHANGE, MqConstant.EMAIL_HTML_KEY, mailDTO);
            //CompletableFuture.runAsync(() -> emailService.sendHtmlMail(mailDTO), threadPoolTaskExecutor);
        } else {
            // 审核提醒
            String adminEmail = userMapper.selectOne(new LambdaQueryWrapper<User>()
                    .select(User::getEmail)
                    .eq(User::getId, CommonConstant.BLOGGER_ID)).getEmail();
            mailDTO.setToEmail(adminEmail);
            mailDTO.setSubject(CommonConstant.CHECK_REMIND);
            mailDTO.setContent("您收到一条新的回复，请前往后台管理页面审核");
            // 发送普通邮件
            rabbitTemplate.convertAndSend(MqConstant.EMAIL_EXCHANGE, MqConstant.EMAIL_SIMPLE_KEY, mailDTO);
            //CompletableFuture.runAsync(() -> emailService.sendSimpleMail(mailDTO), threadPoolTaskExecutor);
        }
    }
}




