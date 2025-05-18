package com.shelly.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.shelly.entity.pojo.ArticleTag;
import com.shelly.entity.pojo.Tag;
import com.shelly.entity.vo.res.TagBackResp;
import com.shelly.entity.vo.res.TagOptionResp;
import com.shelly.entity.vo.req.TagReq;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.query.ArticleConditionQuery;
import com.shelly.entity.vo.query.TagQuery;
import com.shelly.entity.vo.res.ArticleConditionList;
import com.shelly.entity.vo.res.ArticleConditionResp;
import com.shelly.entity.vo.res.TagResp;
import com.shelly.mapper.ArticleMapper;
import com.shelly.mapper.ArticleTagMapper;
import com.shelly.service.TagService;
import com.shelly.mapper.TagMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
* @author Shelly6
* @description 针对表【t_tag】的数据库操作Service实现
* @createDate 2024-07-22 20:24:46
*/
@Service
@RequiredArgsConstructor
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService {
    private final TagMapper tagMapper;
    private final ArticleTagMapper articleTagMapper;
    private final ArticleMapper articleMapper;
    @Override
    public void addTag(TagReq tag) {
        Tag tag1 = tagMapper.selectOne(new LambdaQueryWrapper<Tag>()
                .select(Tag::getId)
                .eq(Tag::getTagName,tag.getTagName()));
        if(tag1 != null){
            throw new IllegalArgumentException("标签已存在");
        }
        Tag newTag = Tag.builder()
                .tagName(tag.getTagName())
                .build();
        baseMapper.insert(newTag);
    }

    @Override
    public void deleteTag(List<Integer> tagIdList) {
        // 标签下是否有文章
        Long count = articleTagMapper.selectCount(new LambdaQueryWrapper<ArticleTag>()
                .in(ArticleTag::getTagId, tagIdList));
        Assert.isFalse(count > 0, "删除失败，标签下存在文章");
        // 批量删除标签
        tagMapper.deleteBatchIds(tagIdList);
    }

    @Override
    public PageResult<TagBackResp> listTag(TagQuery tagQuery) {
        // 查询标签数量
        Long count = tagMapper.selectCount(new LambdaQueryWrapper<Tag>()
                .like(StringUtils.hasText(tagQuery.getKeyword()), Tag::getTagName, tagQuery.getKeyword()));
        if (count == 0) {
            return new PageResult<>();
        }
        // 分页查询标签列表
        List<TagBackResp> tagList = tagMapper.selectBackTagList(tagQuery);
        return new PageResult<>(tagList, count);
    }

    @Override
    public List<TagOptionResp> listTagOption() {
        return tagMapper.selectTagOptionList();
    }

    @Override
    public void updateTag(TagReq tag) {
        // 标签是否存在
        Tag existTag = tagMapper.selectOne(new LambdaQueryWrapper<Tag>()
                .select(Tag::getId)
                .eq(Tag::getTagName, tag.getTagName()));
        Assert.isFalse(Objects.nonNull(existTag) && !existTag.getId().equals(tag.getId()),
                tag.getTagName() + "标签已存在");
        // 修改标签
        Tag newTag = Tag.builder()
                .id(tag.getId())
                .tagName(tag.getTagName())
                .build();
        baseMapper.updateById(newTag);
    }

    @Override
    public ArticleConditionList listTagByArticle(ArticleConditionQuery tagQuery) {
        List<ArticleConditionResp> articleConditionList = articleMapper.selectArticleListByCondition(tagQuery);
        String name = tagMapper.selectOne(new LambdaQueryWrapper<Tag>()
                        .select(Tag::getTagName)
                        .eq(Tag::getId, tagQuery.getTagId()))
                .getTagName();
        return ArticleConditionList.builder()
                .articleConditionVOList(articleConditionList)
                .name(name)
                .build();
    }

    @Override
    public List<TagResp> getTag() {
        return tagMapper.selectTagVOList();
    }
}




