package com.shelly.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.shelly.entity.pojo.Photo;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.query.PhotoQuery;
import com.shelly.entity.vo.req.PhotoInfoReq;
import com.shelly.entity.vo.req.PhotoReq;
import com.shelly.entity.vo.res.AlbumBackResp;
import com.shelly.entity.vo.res.PhotoBackResp;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
* @author Shelly6
* @description 针对表【t_photo】的数据库操作Service
* @createDate 2024-07-22 20:24:46
*/
public interface PhotoService extends IService<Photo> {

    void addPhoto(PhotoReq photo);

    AlbumBackResp getAlbumInfo(Integer albumId);

    void deletePhoto(List<Integer> photoIdList);

    PageResult<PhotoBackResp> listPhoto(PhotoQuery photoQuery);

    void updatePhoto(PhotoReq photo);

    void updatePhotoAlbum(PhotoInfoReq photoInfo);

    String uploadPhoto(MultipartFile file);

    Map<String, Object> listPublicPhoto(Integer albumId);
}
