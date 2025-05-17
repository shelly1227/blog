package com.shelly.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shelly.entity.pojo.Role;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.Query.RoleQuery;
import com.shelly.entity.vo.Request.RoleReq;
import com.shelly.entity.vo.Request.RoleStatusReq;
import com.shelly.entity.vo.Response.RoleResp;

import java.util.List;

/**
* @author Shelly6
* @description 针对表【t_role】的数据库操作Service
* @createDate 2024-07-22 20:24:46
*/
public interface RoleService extends IService<Role> {

    PageResult<RoleResp> listRoleVO(RoleQuery roleQuery);

    void addRole(RoleReq role);

    void deleteRole(List<String> roleIdList);

    void updateRoleStatus(RoleStatusReq roleStatus);

    List<Integer> listRoleMenuTree(String roleId);

    void updateRole(RoleReq role);
}
