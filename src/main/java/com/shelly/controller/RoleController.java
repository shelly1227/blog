package com.shelly.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import com.shelly.common.Result;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.Query.RoleQuery;
import com.shelly.entity.vo.Request.RoleReq;
import com.shelly.entity.vo.Request.RoleStatusReq;
import com.shelly.entity.vo.Response.RoleResp;
import com.shelly.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;



    @SaCheckPermission("system:role:list")
    @GetMapping("/admin/role/list")
    public Result<PageResult<RoleResp>> listRoleVO(RoleQuery roleQuery) {
        return Result.success(roleService.listRoleVO(roleQuery));
    }




    @SaCheckPermission("system:role:add")
    @PostMapping("/admin/role/add")
    public Result<?> addRole(@Validated @RequestBody RoleReq role) {
        roleService.addRole(role);
        return Result.success();
    }


    @SaCheckPermission("system:role:delete")
    @DeleteMapping("/admin/role/delete")
    public Result<?> deleteRole(@RequestBody List<String> roleIdList) {
        roleService.deleteRole(roleIdList);
        return Result.success();
    }


    @SaCheckPermission("system:role:update")
    @PutMapping("/admin/role/update")
    public Result<?> updateRole(@Validated @RequestBody RoleReq role) {
        roleService.updateRole(role);
        return Result.success();
    }


    @SaCheckPermission(value = {"system:role:update", "system:role:status"}, mode = SaMode.OR)
    @PutMapping("/admin/role/changeStatus")
    public Result<?> updateRoleStatus(@Validated @RequestBody RoleStatusReq roleStatus) {
        roleService.updateRoleStatus(roleStatus);
        return Result.success();
    }


    @SaCheckPermission("system:role:list")
    @GetMapping("/admin/role/menu/{roleId}")
    public Result<List<Integer>> listRoleMenuTree(@PathVariable("roleId") String roleId) {
        return Result.success(roleService.listRoleMenuTree(roleId));
    }

}