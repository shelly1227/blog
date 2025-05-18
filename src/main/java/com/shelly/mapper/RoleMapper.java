package com.shelly.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shelly.entity.pojo.Role;
import com.shelly.entity.vo.query.RoleQuery;
import com.shelly.entity.vo.res.RoleResp;
import com.shelly.entity.vo.res.UserRoleResp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Shelly6
* @description 针对表【t_role】的数据库操作Mapper
* @createDate 2024-07-22 20:24:46
* @Entity generator.entity.Role
*/
public interface RoleMapper extends BaseMapper<Role> {

    List<RoleResp> selectBackRoleList(@Param("param") RoleQuery roleQuery);

    List<String> selectRoleListByUserId(@Param("userId") Object userId);

    List<UserRoleResp> selectUserRoleList();
}




