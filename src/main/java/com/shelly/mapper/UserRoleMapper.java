package com.shelly.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shelly.entity.pojo.UserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Shelly6
* @description 针对表【t_user_role】的数据库操作Mapper
* @createDate 2024-07-22 20:24:46
* @Entity generator.entity.UserRole
*/
public interface UserRoleMapper extends BaseMapper<UserRole> {

    void insertUserRole(@Param("userId") Integer userId, @Param("roleIdList") List<String> roleIdList);
}




