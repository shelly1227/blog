package com.shelly.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shelly.entity.pojo.RoleMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Shelly6
* @description 针对表【t_role_menu】的数据库操作Mapper
* @createDate 2024-07-22 20:24:46
* @Entity generator.entity.RoleMenu
*/
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    void insertRoleMenu(@Param("roleId") String id, List<Integer> menuIdList);

    void deleteRoleMenu(List<String> roleIdList);

    List<Integer> selectMenuByRoleId(String roleId);

    void deleteRoleMenuByRoleId(@Param("roleId")String id);
}




