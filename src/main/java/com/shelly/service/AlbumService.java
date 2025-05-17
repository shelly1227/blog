package com.shelly.service;

import com.shelly.entity.pojo.Album;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.Query.AlbumQuery;
import com.shelly.entity.vo.Request.AlbumReq;
import com.shelly.entity.vo.Response.AlbumBackResp;
import com.shelly.entity.vo.Response.AlbumResp;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
* @author Shelly6
* @description 针对表【t_album】的数据库操作Service
* @createDate 2024-07-22 20:24:46
*/
public interface AlbumService extends IService<Album> {

    void addAlbum(AlbumReq album);

    void deleteAlbum(Integer albumId);

    AlbumReq editAlbum(Integer albumId);

    PageResult<AlbumBackResp> listAlbumBackVO(AlbumQuery albumQuery);

    String uploadAlbumCover(MultipartFile file);

    void updateAlbum(AlbumReq album);

    List<AlbumResp> listAlbumVO();
}
