package com.shelly.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shelly.entity.pojo.Menu;
import com.shelly.entity.vo.Query.MenuQuery;
import com.shelly.entity.vo.Response.MenuOptionResp;
import com.shelly.entity.vo.Response.MenuResp;
import com.shelly.entity.vo.Response.MenuTreeResp;
import com.shelly.entity.vo.Response.UserMenuResp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Shelly6
* @description 针对表【t_menu】的数据库操作Mapper
* @createDate 2024-07-22 20:24:46
* @Entity generator.entity.Menu
*/
public interface MenuMapper extends BaseMapper<Menu> {

    List<MenuOptionResp> selectMenuOptions();

    List<MenuTreeResp> selectMenuTree();

    List<MenuResp> selectMenuVOList(@Param("param") MenuQuery menuQuery);

    List<String> selectPermissionByRoleId(@Param("roleId") String roleId);

    List<UserMenuResp> selectMenuByUserId(@Param("userId") Integer userId);
}




