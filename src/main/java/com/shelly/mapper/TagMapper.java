package com.shelly.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shelly.entity.pojo.Tag;
import com.shelly.entity.vo.res.TagBackResp;
import com.shelly.entity.vo.res.TagOptionResp;
import com.shelly.entity.vo.query.TagQuery;
import com.shelly.entity.vo.res.TagResp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Shelly6
* @description 针对表【t_tag】的数据库操作Mapper
* @createDate 2024-07-22 20:24:46
* @Entity generator.entity.Tag
*/
public interface TagMapper extends BaseMapper<Tag> {

    List<TagBackResp> selectBackTagList(@Param("param")TagQuery tagQuery);

    List<TagOptionResp> selectTagOptionList();

    List<TagResp> selectTagVOList();

    List<String> selectTagNameByArticleId(@Param("articleId")Integer articleId);
}




