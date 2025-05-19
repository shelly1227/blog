package com.shelly.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import com.shelly.common.Result;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.query.RoleQuery;
import com.shelly.entity.vo.req.RoleReq;
import com.shelly.entity.vo.req.RoleStatusReq;
import com.shelly.entity.vo.res.RoleResp;
import com.shelly.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "角色模块")
//checked but session
public class RoleController {

    private final RoleService roleService;



    @SaCheckPermission("system:role:list")
    @GetMapping("/admin/role/list")
    @Operation(summary = "获取角色列表")
    public Result<PageResult<RoleResp>> listRoleVO(RoleQuery roleQuery) {
        return Result.success(roleService.listRoleVO(roleQuery));
    }




    @SaCheckPermission("system:role:add")
    @PostMapping("/admin/role/add")
    @Operation(summary = "添加角色")
    public Result<?> addRole(@Validated @RequestBody RoleReq role) {
        roleService.addRole(role);
        return Result.success();
    }


    @SaCheckPermission("system:role:delete")
    @Operation(summary = "删除角色")
    @DeleteMapping("/admin/role/delete")
    public Result<?> deleteRole(@RequestBody List<String> roleIdList) {
        roleService.deleteRole(roleIdList);
        return Result.success();
    }


    @SaCheckPermission("system:role:update")
    @PutMapping("/admin/role/update")
    @Operation(summary = "修改角色")
    public Result<?> updateRole(@Validated @RequestBody RoleReq role) {
        roleService.updateRole(role);
        return Result.success();
    }


    @SaCheckPermission(value = {"system:role:update", "system:role:status"}, mode = SaMode.OR)
    @PutMapping("/admin/role/changeStatus")
    @Operation(summary = "修改角色状态")
    public Result<?> updateRoleStatus(@Validated @RequestBody RoleStatusReq roleStatus) {
        roleService.updateRoleStatus(roleStatus);
        return Result.success();
    }


    @SaCheckPermission("system:role:list")
    @GetMapping("/admin/role/menu/{roleId}")
    @Operation(summary = "获取角色菜单树")
    public Result<List<Integer>> listRoleMenuTree(@PathVariable("roleId") String roleId) {
        return Result.success(roleService.listRoleMenuTree(roleId));
    }

}