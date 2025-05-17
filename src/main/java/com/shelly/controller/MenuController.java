package com.shelly.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shelly.common.Result;
import com.shelly.entity.vo.Query.MenuQuery;
import com.shelly.entity.vo.Request.MenuReq;
import com.shelly.entity.vo.Response.MenuOptionResp;
import com.shelly.entity.vo.Response.MenuResp;
import com.shelly.entity.vo.Response.MenuTreeResp;
import com.shelly.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单控制器
 */
@RestController
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

    @SaCheckPermission("system:menu:add")
    @PostMapping("/admin/menu/add")
    public Result<?> addMenu(@Validated @RequestBody MenuReq menu) {
        menuService.addMenu(menu);
        return Result.success();
    }

    @SaCheckPermission("system:menu:delete")
    @DeleteMapping("/admin/menu/delete/{menuId}")
    public Result<?> deleteMenu(@PathVariable("menuId") Integer menuId) {
        menuService.deleteMenu(menuId);
        return Result.success();
    }

    @SaCheckPermission("system:menu:edit")
    @GetMapping("/admin/menu/edit/{menuId}")
    public Result<MenuReq> editMenu(@PathVariable("menuId") Integer menuId) {
        return Result.success(menuService.editMenu(menuId));
    }

    @SaCheckPermission("system:menu:list")
    @GetMapping("/admin/menu/getMenuOptions")
    public Result<List<MenuOptionResp>> listMenuOption() {
        return Result.success(menuService.listMenuOption());
    }

    @SaCheckPermission("system:role:list")
    @GetMapping("/admin/menu/getMenuTree")
    public Result<List<MenuTreeResp>> listMenuTree() {
        return Result.success(menuService.listMenuTree());
    }



    @SaCheckPermission("system:menu:list")
    @GetMapping("/admin/menu/list")
    public Result<List<MenuResp>> listMenuVO(MenuQuery menuQuery) {
        return Result.success(menuService.listMenuVO(menuQuery));
    }


    @SaCheckPermission("system:menu:update")
    @PutMapping("/admin/menu/update")
    public Result<?> updateMenu(@Validated @RequestBody MenuReq menu) {
        menuService.updateMenu(menu);
        return Result.success();
    }








}
