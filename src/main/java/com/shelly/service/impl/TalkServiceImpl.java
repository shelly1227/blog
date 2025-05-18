package com.shelly.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.shelly.entity.pojo.Talk;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.query.PageQuery;
import com.shelly.entity.vo.query.TalkQuery;
import com.shelly.entity.vo.req.TalkReq;
import com.shelly.entity.vo.res.CommentCountResp;
import com.shelly.entity.vo.res.TalkBackInfoResp;
import com.shelly.entity.vo.res.TalkBackResp;
import com.shelly.entity.vo.res.TalkResp;
import com.shelly.enums.FilePathEnum;
import com.shelly.mapper.CommentMapper;
import com.shelly.service.RedisService;
import com.shelly.service.TalkService;
import com.shelly.mapper.TalkMapper;
import com.shelly.strategy.context.UploadStrategyContext;
import com.shelly.utils.CommonUtils;
import com.shelly.utils.HTMLUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.shelly.constants.RedisConstant.TALK_LIKE_COUNT;
import static com.shelly.enums.ArticleStatusEnum.PUBLIC;
import static com.shelly.enums.CommentTypeEnum.TALK;

/**
* @author Shelly6
* @description 针对表【t_talk】的数据库操作Service实现
* @createDate 2024-07-22 20:24:46
*/
@Service
@RequiredArgsConstructor
public class TalkServiceImpl extends ServiceImpl<TalkMapper, Talk>
    implements TalkService {
    private final TalkMapper talkMapper;
    private final BlogFileServiceImpl blogFileService;
    private final UploadStrategyContext uploadStrategyContext;
    private final RedisService redisService;
    private final CommentMapper commentMapper;

    @Override
    public PageResult<TalkBackResp> listTalkBackVO(TalkQuery talkQuery) {
        // 查询说说数量
        Long count = talkMapper.selectCount(new LambdaQueryWrapper<Talk>()
                .eq(Objects.nonNull(talkQuery.getStatus()), Talk::getStatus, talkQuery.getStatus()));
        if (count == 0) {
            return new PageResult<>();
        }
        // 分页查询说说列表
        List<TalkBackResp> talkBackRespList = talkMapper.selectBackTalkList(talkQuery);
        talkBackRespList.forEach(item -> {
            // 转换图片格式
            if (Objects.nonNull(item.getImages())) {
                item.setImgList(CommonUtils.castList(JSON.parseObject(item.getImages(), List.class), String.class));
            }
        });
        return new PageResult<>(talkBackRespList, count);
    }

    @Override
    public String uploadTalkCover(MultipartFile file) {
        // 上传文件
        String url = uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.TALK.getPath());
        blogFileService.saveBlogFile(file, url, FilePathEnum.TALK.getFilePath());
        return url;
    }

    @Override
    public void addTalk(TalkReq talk) {
        Talk newTalk = new Talk();
        BeanUtils.copyProperties(talk, newTalk);
        newTalk.setUserId(StpUtil.getLoginIdAsInt());
        baseMapper.insert(newTalk);
    }

    @Override
    public void delete(Integer talkId) {
        talkMapper.deleteById(talkId);
    }

    @Override
    public void updateTalk(TalkReq talk) {
        Talk newTalk = new Talk();
        BeanUtils.copyProperties(talk, newTalk);
        newTalk.setUserId(StpUtil.getLoginIdAsInt());
        baseMapper.updateById(newTalk);
    }

    @Override
    public TalkBackInfoResp editTalk(Integer talkId) {
        TalkBackInfoResp talkBackVO = talkMapper.selectTalkBackById(talkId);
        // 转换图片格式
        if (Objects.nonNull(talkBackVO.getImages())) {
            talkBackVO.setImgList(CommonUtils.castList(JSON.parseObject(talkBackVO.getImages(), List.class), String.class));
        }
        return talkBackVO;
    }

    @Override
    public List<String> listTalkHome() {
        // 查询最新5条说说
        List<Talk> talkList = talkMapper.selectList(new LambdaQueryWrapper<Talk>()
                .select(Talk::getTalkContent)
                .eq(Talk::getStatus, PUBLIC.getStatus())
                .orderByDesc(Talk::getIsTop)
                .orderByDesc(Talk::getId)
                .last("limit 5"));
        return talkList.stream()
                .map(item -> item.getTalkContent().length() > 200
                        ? HTMLUtils.deleteHtmlTag(item.getTalkContent().substring(0, 200))
                        : HTMLUtils.deleteHtmlTag(item.getTalkContent()))
                .collect(Collectors.toList());
    }

    @Override
    public PageResult<TalkResp> listTalkVO(PageQuery pageQuery) {
        Long count = talkMapper.selectCount((new LambdaQueryWrapper<Talk>()
                .eq(Talk::getStatus, PUBLIC.getStatus())));
        if (count == 0) {
            return new PageResult<>();
        }
        // 分页查询说说
        List<TalkResp> talkRespList = talkMapper.selectTalkList(pageQuery);
        // 查询说说评论量
        List<Integer> talkIdList = talkRespList.stream()
                .map(TalkResp::getId)
                .collect(Collectors.toList());
        List<CommentCountResp> commentCountVOList = commentMapper.selectCommentCountByTypeId(talkIdList, TALK.getType());
        Map<Integer, Integer> commentCountMap = commentCountVOList.stream()
                .collect(Collectors.toMap(CommentCountResp::getId, CommentCountResp::getCommentCount));
        // 查询说说点赞量
        Map<String, Integer> likeCountMap = redisService.getHashAll(TALK_LIKE_COUNT);
        // 封装说说
        talkRespList.forEach(item -> {
            item.setLikeCount(Optional.ofNullable(likeCountMap.get(item.getId().toString())).orElse(0));
            item.setCommentCount(Optional.ofNullable(commentCountMap.get(item.getId())).orElse(0));
            // 转换图片格式
            if (Objects.nonNull(item.getImages())) {
                item.setImgList(CommonUtils.castList(JSON.parseObject(item.getImages(), List.class), String.class));
            }
        });
        return new PageResult<>(talkRespList, count);
    }

    @Override
    public TalkResp getTalkById(Integer talkId) {
        // 查询说说信息
        TalkResp talkResp = talkMapper.selectTalkById(talkId);
        if (Objects.isNull(talkResp)) {
            return null;
        }
        // 查询说说点赞量
        Integer likeCount = redisService.getHash(TALK_LIKE_COUNT, talkId.toString());
        talkResp.setLikeCount(Optional.ofNullable(likeCount).orElse(0));
        // 转换图片格式
        if (Objects.nonNull(talkResp.getImages())) {
            talkResp.setImgList(CommonUtils.castList(JSON.parseObject(talkResp.getImages(), List.class), String.class));
        }
        return talkResp;
    }
}




