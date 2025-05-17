package com.shelly.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shelly.entity.pojo.Carousel;
import com.shelly.entity.vo.Query.CarouselQuery;
import com.shelly.entity.vo.Response.CarouselBackResp;
import com.shelly.entity.vo.Response.CarouselResp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Shelly6
* @description 针对表【t_carousel】的数据库操作Mapper
* @createDate 2024-07-22 20:24:46
* @Entity generator.entity.Carousel
*/
public interface CarouselMapper extends BaseMapper<Carousel> {

    List<CarouselResp> selectCarouselList();

    List<CarouselBackResp> selectBackCarouselList(@Param("param")CarouselQuery carouselQuery);
}




