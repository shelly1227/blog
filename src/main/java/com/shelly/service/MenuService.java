package com.shelly.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.shelly.entity.pojo.Menu;
import com.shelly.entity.vo.Query.MenuQuery;
import com.shelly.entity.vo.Request.MenuReq;
import com.shelly.entity.vo.Response.MenuOptionResp;
import com.shelly.entity.vo.Response.MenuResp;
import com.shelly.entity.vo.Response.MenuTreeResp;

import java.util.List;

/**
* @author Shelly6
* @description 针对表【t_menu】的数据库操作Service
* @createDate 2024-07-22 20:24:46
*/
public interface MenuService extends IService<Menu> {

    void addMenu(MenuReq menu);

    void deleteMenu(Integer menuId);

    MenuReq editMenu(Integer menuId);

    List<MenuOptionResp> listMenuOption();

    List<MenuTreeResp> listMenuTree();

    List<MenuResp> listMenuVO(MenuQuery menuQuery);

    void updateMenu(MenuReq menu);
}
