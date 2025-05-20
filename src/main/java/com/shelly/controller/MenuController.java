package com.shelly.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shelly.annotation.OptLogger;
import com.shelly.common.Result;
import com.shelly.entity.vo.query.MenuQuery;
import com.shelly.entity.vo.req.MenuReq;
import com.shelly.entity.vo.res.MenuOptionResp;
import com.shelly.entity.vo.res.MenuResp;
import com.shelly.entity.vo.res.MenuTreeResp;
import com.shelly.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.shelly.constants.OptTypeConstant.ADD;
import static com.shelly.constants.OptTypeConstant.DELETE;
import static com.shelly.constants.OptTypeConstant.UPDATE;

/**
 * 菜单控制器
 * @author shelly
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "菜单模块")
//checked
public class MenuController {
    private final MenuService menuService;

    @SaCheckPermission("system:menu:add")
    @PostMapping("/admin/menu/add")
    @Operation(summary = "添加菜单")
    @OptLogger(value = ADD)
    public Result<?> addMenu(@Validated @RequestBody MenuReq menu) {
        menuService.addMenu(menu);
        return Result.success();
    }

    @SaCheckPermission("system:menu:delete")
    @DeleteMapping("/admin/menu/delete/{menuId}")
    @Operation(summary = "删除菜单")
    @OptLogger(value = DELETE)
    public Result<?> deleteMenu(@PathVariable("menuId") Integer menuId) {
        menuService.deleteMenu(menuId);
        return Result.success();
    }

    @SaCheckPermission("system:menu:edit")
    @GetMapping("/admin/menu/edit/{menuId}")
    @Operation(summary = "编辑菜单")
    public Result<MenuReq> editMenu(@PathVariable("menuId") Integer menuId) {
        return Result.success(menuService.editMenu(menuId));
    }

    @SaCheckPermission("system:menu:list")
    @Operation(summary = "获取菜单下拉列表")
    @GetMapping("/admin/menu/getMenuOptions")
    public Result<List<MenuOptionResp>> listMenuOption() {
        return Result.success(menuService.listMenuOption());
    }

    @SaCheckPermission("system:role:list")
    @GetMapping("/admin/menu/getMenuTree")
    @Operation(summary = "获取菜单树形列表")
    public Result<List<MenuTreeResp>> listMenuTree() {
        return Result.success(menuService.listMenuTree());
    }



    @SaCheckPermission("system:menu:list")
    @GetMapping("/admin/menu/list")
    @Operation(summary = "获取菜单列表")
    public Result<List<MenuResp>> listMenuVO(MenuQuery menuQuery) {
        return Result.success(menuService.listMenuVO(menuQuery));
    }


    @SaCheckPermission("system:menu:update")
    @PutMapping("/admin/menu/update")
    @Operation(summary = "修改菜单")
    @OptLogger(value = UPDATE)
    public Result<?> updateMenu(@Validated @RequestBody MenuReq menu) {
        menuService.updateMenu(menu);
        return Result.success();
    }
}
