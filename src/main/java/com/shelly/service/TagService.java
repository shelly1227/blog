package com.shelly.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.shelly.entity.pojo.Tag;
import com.shelly.entity.vo.Response.TagBackResp;
import com.shelly.entity.vo.Response.TagOptionResp;
import com.shelly.entity.vo.Request.TagReq;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.Query.ArticleConditionQuery;
import com.shelly.entity.vo.Query.TagQuery;
import com.shelly.entity.vo.Response.ArticleConditionList;
import com.shelly.entity.vo.Response.TagResp;

import java.util.List;

/**
* @author Shelly6
* @description 针对表【t_tag】的数据库操作Service
* @createDate 2024-07-22 20:24:46
*/
public interface TagService extends IService<Tag> {

    void addTag(TagReq tag);

    void deleteTag(List<Integer> tagIdList);

    PageResult<TagBackResp> listTag(TagQuery tagQuery);

    List<TagOptionResp> listTagOption();

    void updateTag(TagReq tag);

    ArticleConditionList listTagByArticle(ArticleConditionQuery tagQuery);

    List<TagResp> getTag();
}
