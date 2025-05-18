package com.shelly.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shelly.entity.pojo.Category;
import com.shelly.entity.vo.res.CategoryBackResp;
import com.shelly.entity.vo.res.CategoryResp;
import com.shelly.entity.vo.query.CategoryQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Shelly6
* @description 针对表【t_category】的数据库操作Mapper
* @createDate 2024-07-22 20:24:46
* @Entity generator.entity.Category
*/
public interface CategoryMapper extends BaseMapper<Category> {
    //查询出所有分类的id，文章数量，分类名，父级id，创建时间
    List<CategoryBackResp> selectBackCategoryList(@Param("param") CategoryQuery categoryQuery);

    List<CategoryResp> listCategoryVO();

    List<CategoryResp> selectCategoryVO();
}




