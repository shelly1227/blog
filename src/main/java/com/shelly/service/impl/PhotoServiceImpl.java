package com.shelly.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shelly.entity.pojo.Album;

import com.shelly.entity.pojo.Photo;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.Query.PhotoQuery;
import com.shelly.entity.vo.Request.PhotoInfoReq;
import com.shelly.entity.vo.Request.PhotoReq;
import com.shelly.entity.vo.Response.AlbumBackResp;
import com.shelly.entity.vo.Response.PhotoBackResp;
import com.shelly.entity.vo.Response.PhotoResp;
import com.shelly.enums.FilePathEnum;
import com.shelly.mapper.AlbumMapper;
import com.shelly.service.PhotoService;
import com.shelly.mapper.PhotoMapper;
import com.shelly.strategy.context.UploadStrategyContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
* @author Shelly6
* @description 针对表【t_photo】的数据库操作Service实现
* @createDate 2024-07-22 20:24:46
*/
@Service
@RequiredArgsConstructor
public class PhotoServiceImpl extends ServiceImpl<PhotoMapper, Photo>
    implements PhotoService{

    private final PhotoMapper photoMapper;

    private final AlbumMapper albumMapper;

    private final UploadStrategyContext uploadStrategyContext;

    private final BlogFileServiceImpl blogFileService;
    @Override
    @Transactional
    public void addPhoto(PhotoReq photo) {
        // 批量保存照片
        List<Photo> pictureList = photo.getPhotoUrlList().stream()
                .map(url -> Photo.builder()
                        .albumId(photo.getAlbumId())
                        .photoName(IdWorker.getIdStr())
                        .photoUrl(url)
                        .build())
                .collect(Collectors.toList());
        this.saveBatch(pictureList);
    }

    @Override
    public AlbumBackResp getAlbumInfo(Integer albumId) {
        LambdaQueryWrapper<Album> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.select(Album::getId, Album::getAlbumName, Album::getAlbumDesc, Album::getAlbumCover, Album::getStatus)
                .eq(Album::getId, albumId);
        Album album = albumMapper.selectOne(queryWrapper);
        if (Objects.isNull(album)) {
            return null;
        }

        AlbumBackResp albumBackResp = new AlbumBackResp();
        BeanUtils.copyProperties(album, albumBackResp);
        Long photoCount = photoMapper.selectCount(new LambdaQueryWrapper<Photo>()
                .eq(Photo::getAlbumId, albumId));
        albumBackResp.setPhotoCount(photoCount);
        return albumBackResp;
    }

    @Override
    public void deletePhoto(List<Integer> photoIdList) {
        baseMapper.deleteBatchIds(photoIdList);
    }

    @Override
    public PageResult<PhotoBackResp> listPhoto(PhotoQuery photoQuery) {
        // 查询照片数量
        Long count = photoMapper.selectCount(new LambdaQueryWrapper<Photo>()
                .eq(Objects.nonNull(photoQuery.getAlbumId()), Photo::getAlbumId, photoQuery.getAlbumId()));
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询照片列表
        List<PhotoBackResp> photoList = photoMapper.selectBackPhotoList(photoQuery);
        return new PageResult<>(photoList, count);
    }

    @Override
    @Transactional
    public void updatePhoto(PhotoReq photo) {
        List<Photo> photoList = photo.getPhotoIdList().stream()
                .map(photoId -> Photo.builder()
                        .id(photoId)
                        .albumId(photo.getAlbumId())
                        .build())
                .collect(Collectors.toList());
        this.updateBatchById(photoList);
    }

    @Override
    public void updatePhotoAlbum(PhotoInfoReq photoInfo) {
        Photo photo = new Photo();
        BeanUtils.copyProperties(photoInfo, photo);
        this.updateById(photo);
    }

    @Override
    public String uploadPhoto(MultipartFile file) {
        // 上传文件
        String url = uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.PHOTO.getPath());
        blogFileService.saveBlogFile(file, url, FilePathEnum.PHOTO.getFilePath());
        return url;
    }

    @Override
    public Map<String, Object> listPublicPhoto(Integer albumId) {
        Map<String, Object> result = new HashMap<>(2);
        String albumName = albumMapper.selectOne(new LambdaQueryWrapper<Album>()
                        .select(Album::getAlbumName).eq(Album::getId, albumId))
                .getAlbumName();
        List<Photo>list = photoMapper.selectList(new LambdaQueryWrapper<Photo>()
                .select(Photo::getId, Photo::getPhotoName, Photo::getPhotoUrl)
                .eq(Photo::getAlbumId, albumId));
        List<PhotoResp> photoVOList = list.stream()
                .map(item -> {
                    PhotoResp photoVO = new PhotoResp();
                    BeanUtils.copyProperties(item, photoVO);
                    return photoVO;
                }).collect(Collectors.toList());
        result.put("albumName", albumName);
        result.put("photoVOList", photoVOList);
        return result;
    }
}




