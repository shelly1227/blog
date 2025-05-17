package com.shelly.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shelly.entity.pojo.Photo;
import com.shelly.entity.vo.Query.PhotoQuery;
import com.shelly.entity.vo.Response.PhotoBackResp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Shelly6
* @description 针对表【t_photo】的数据库操作Mapper
* @createDate 2024-07-22 20:24:46
* @Entity generator.entity.Photo
*/
public interface PhotoMapper extends BaseMapper<Photo> {

    List<PhotoBackResp> selectBackPhotoList(@Param("param")PhotoQuery photoQuery);
}




