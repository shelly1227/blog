package com.shelly.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shelly.entity.pojo.Carousel;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.Query.CarouselQuery;
import com.shelly.entity.vo.Request.CarouselReqVo;
import com.shelly.entity.vo.Request.CarouselStatusReq;
import com.shelly.entity.vo.Response.CarouselBackResp;
import com.shelly.entity.vo.Response.CarouselResp;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
* @author Shelly6
* @description 针对表【t_carousel】的数据库操作Service
* @createDate 2024-07-22 20:24:46
*/
public interface CarouselService extends IService<Carousel> {

    List<CarouselResp> getCarouselList();

    PageResult<CarouselBackResp> getCarouselVOList(CarouselQuery carouselQuery);

    String uploadCarousel(MultipartFile file);

    void updateCarousel(CarouselReqVo carouselReqVo);

    void updateCarouselStatus(CarouselStatusReq carouselStatusReq);

    void addCarousel(CarouselReqVo carouselReqVo);
}
