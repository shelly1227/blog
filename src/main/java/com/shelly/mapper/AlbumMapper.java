package com.shelly.mapper;

import com.shelly.entity.pojo.Album;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shelly.entity.vo.query.AlbumQuery;
import com.shelly.entity.vo.res.AlbumBackResp;
import com.shelly.entity.vo.res.AlbumResp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Shelly6
* @description 针对表【t_album】的数据库操作Mapper
* @createDate 2024-07-22 20:24:46
* @Entity generator.entity.Album
*/
public interface AlbumMapper extends BaseMapper<Album> {

    List<AlbumBackResp> selectBackAlbumList(@Param("param")AlbumQuery albumQuery);

    List<AlbumResp> selectAlbumVOList();
}




