package com.shelly.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shelly.entity.pojo.Carousel;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.query.CarouselQuery;
import com.shelly.entity.vo.req.CarouselReqVo;
import com.shelly.entity.vo.req.CarouselStatusReq;
import com.shelly.entity.vo.res.CarouselBackResp;
import com.shelly.entity.vo.res.CarouselResp;
import com.shelly.enums.FilePathEnum;
import com.shelly.service.CarouselService;
import com.shelly.mapper.CarouselMapper;
import com.shelly.strategy.context.UploadStrategyContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
* @author Shelly6
* @description 针对表【t_carousel】的数据库操作Service实现
* @createDate 2024-07-22 20:24:46
*/
@Service
@RequiredArgsConstructor
public class CarouselServiceImpl extends ServiceImpl<CarouselMapper, Carousel>
    implements CarouselService{
    private final CarouselMapper carouselMapper;
    private final BlogFileServiceImpl blogFileService;
    private final UploadStrategyContext uploadStrategyContext;

    @Override
    public List<CarouselResp> getCarouselList() {
        return carouselMapper.selectCarouselList();
    }

    @Override
    public PageResult<CarouselBackResp> getCarouselVOList(CarouselQuery carouselQuery) {
        // 查询轮播图数量
        Long count = carouselMapper.selectCount(null);
        if (count == 0) {
            return new PageResult<>();
        }
        // 分页查询轮播图列表
        List<CarouselBackResp> carouselList = carouselMapper.selectBackCarouselList(carouselQuery);
        return new PageResult<>(carouselList, count);
    }

    @Override
    public String uploadCarousel(MultipartFile file) {
        // 上传文件
        String url = uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.CAROUSEL.getPath());
        blogFileService.saveBlogFile(file, url, FilePathEnum.CAROUSEL.getFilePath());
        return url;
    }

    @Override
    public void updateCarousel(CarouselReqVo carouselReqVo) {
        Assert.notNull(carouselReqVo.getId(), "id is null");
       Carousel carousel = new Carousel();
        BeanUtils.copyProperties(carouselReqVo, carousel);
        carouselMapper.updateById(carousel);
    }

    @Override
    public void updateCarouselStatus(CarouselStatusReq carouselStatusReq) {
        Carousel carousel = Carousel.builder()
                .id(carouselStatusReq.getId())
                .status(carouselStatusReq.getStatus())
                .build();
        carouselMapper.updateById(carousel);
    }

    @Override
    public void addCarousel(CarouselReqVo carouselReqVo) {
        Carousel carousel = new Carousel();
        BeanUtils.copyProperties(carouselReqVo, carousel);
        carouselMapper.insert(carousel);
    }
}




