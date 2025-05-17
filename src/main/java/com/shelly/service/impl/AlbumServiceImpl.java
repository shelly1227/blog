package com.shelly.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shelly.entity.pojo.Album;
import com.shelly.entity.pojo.Photo;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.Query.AlbumQuery;
import com.shelly.entity.vo.Request.AlbumReq;
import com.shelly.entity.vo.Response.AlbumBackResp;
import com.shelly.entity.vo.Response.AlbumResp;
import com.shelly.enums.FilePathEnum;
import com.shelly.mapper.PhotoMapper;
import com.shelly.service.AlbumService;
import com.shelly.mapper.AlbumMapper;
import com.shelly.strategy.context.UploadStrategyContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

/**
* @author Shelly6
* @description 针对表【t_album】的数据库操作Service实现
* @createDate 2024-07-22 20:24:46
*/
@Service
@RequiredArgsConstructor
public class AlbumServiceImpl extends ServiceImpl<AlbumMapper, Album>
    implements AlbumService{
    private final AlbumMapper albumMapper;
    private final PhotoMapper photoMapper;
    private final BlogFileServiceImpl blogFileService;
    private final UploadStrategyContext uploadStrategyContext;

    @Override
    public void addAlbum(AlbumReq album) {
        Album existAlbum = albumMapper.selectOne(new LambdaQueryWrapper<Album>()
                .select(Album::getId)
                .eq(Album::getAlbumName, album.getAlbumName()));
        Assert.isNull(existAlbum, album.getAlbumName() + "相册已存在");
        // 添加新相册
        Album newAlbum = new Album();
        BeanUtils.copyProperties(album, newAlbum);
        baseMapper.insert(newAlbum);
    }

    @Override
    public void deleteAlbum(Integer albumId) {
        // 查询照片数量
        Long count = photoMapper.selectCount(new LambdaQueryWrapper<Photo>()
                .eq(Photo::getAlbumId, albumId));
        Assert.isFalse(count > 0, "相册下存在照片");
        // 不存在照片则删除
        albumMapper.deleteById(albumId);
    }

    @Override
    public AlbumReq editAlbum(Integer albumId) {
        Album album = albumMapper.selectOne(new LambdaQueryWrapper<Album>()
                        .select(Album::getAlbumCover, Album::getAlbumDesc, Album::getAlbumName, Album::getId, Album::getStatus)
                .eq(Album::getId, albumId));
        AlbumReq albumReq = new AlbumReq();
        BeanUtils.copyProperties(album, albumReq);
        return albumReq;
    }

    @Override
    public PageResult<AlbumBackResp> listAlbumBackVO(AlbumQuery albumQuery) {
        // 查询相册数量
        Long count = albumMapper.selectCount(new LambdaQueryWrapper<Album>()
                .like(StringUtils.hasText(albumQuery.getKeyword()), Album::getAlbumName, albumQuery.getKeyword()));
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询相册信息
        List<AlbumBackResp> albumList = albumMapper.selectBackAlbumList(albumQuery);
        return new PageResult<>(albumList, count);
    }

    @Override
    public String uploadAlbumCover(MultipartFile file) {
        String url = uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.PHOTO.getPath());
        blogFileService.saveBlogFile(file, url, FilePathEnum.PHOTO.getFilePath());
        return url;
    }

    @Override
    public void updateAlbum(AlbumReq album) {
        // 相册是否存在
        Album existAlbum = albumMapper.selectOne(new LambdaQueryWrapper<Album>()
                .select(Album::getId)
                .eq(Album::getAlbumName, album.getAlbumName()));
        Assert.isFalse(Objects.nonNull(existAlbum) && !existAlbum.getId().equals(album.getId()),
                album.getAlbumName() + "相册已存在");
        Album newAlbum = new Album();
        BeanUtils.copyProperties(album, newAlbum);
        baseMapper.updateById(newAlbum);
    }

    @Override
    public List<AlbumResp> listAlbumVO() {
        return albumMapper.selectAlbumVOList();
    }
}




