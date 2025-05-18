package com.shelly.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shelly.entity.pojo.Carousel;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.query.CarouselQuery;
import com.shelly.entity.vo.req.CarouselReqVo;
import com.shelly.entity.vo.req.CarouselStatusReq;
import com.shelly.entity.vo.res.CarouselBackResp;
import com.shelly.entity.vo.res.CarouselResp;
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
