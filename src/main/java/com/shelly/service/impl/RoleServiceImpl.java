package com.shelly.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.session.SaSessionCustomUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.shelly.entity.pojo.Role;
import com.shelly.entity.pojo.UserRole;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.query.RoleQuery;
import com.shelly.entity.vo.req.RoleReq;
import com.shelly.entity.vo.req.RoleStatusReq;
import com.shelly.entity.vo.res.RoleResp;
import com.shelly.enums.RedisConstants;
import com.shelly.mapper.RoleMenuMapper;
import com.shelly.mapper.UserRoleMapper;
import com.shelly.service.RoleService;
import com.shelly.mapper.RoleMapper;
import com.shelly.service.UserRoleService;
import com.shelly.utils.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

/**
* @author Shelly6
* @description 针对表【t_role】的数据库操作Service实现
* @createDate 2024-07-22 20:24:46
*/
@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService {
    private final RoleMapper roleMapper;
    private final RoleMenuMapper roleMenuMapper;
    private final UserRoleMapper userRoleMapper;
    private final UserRoleService userRoleService;

    @Override
    public PageResult<RoleResp> listRoleVO(RoleQuery roleQuery) {
        // 查询角色数量
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        if (roleQuery.getKeyword() != null && !roleQuery.getKeyword().isEmpty()) {
            queryWrapper.like(Role::getRoleName, roleQuery.getKeyword());
        }
        if (roleQuery.getIsDisable() != null) {
            queryWrapper.eq(Role::getIsDisable, roleQuery.getIsDisable());
        }
        Long count = roleMapper.selectCount(queryWrapper);
        if (count == 0) {
            return new PageResult<>();
        }
        // 分页查询角色列表
        List<RoleResp> roleRespList = roleMapper.selectBackRoleList(roleQuery);
        return new PageResult<>(roleRespList, count);
    }

    @Override
    public void addRole(RoleReq role) {
        // 角色名是否存在
        Role existRole = roleMapper.selectOne(new LambdaQueryWrapper<Role>()
                .select(Role::getId)
                .eq(Role::getRoleName, role.getRoleName()));
        Assert.isNull(existRole, role.getRoleName() + "角色名已存在");
        // 添加新角色
        Role newRole = Role.builder().roleName(role.getRoleName()).roleDesc(role.getRoleDesc()).isDisable(role.getIsDisable()).build();
        baseMapper.insert(newRole);
        // 添加角色菜单权限
        roleMenuMapper.insertRoleMenu(newRole.getId(), role.getMenuIdList());
    }

    @Override
    public void deleteRole(List<String> roleIdList) {
        Assert.isFalse(roleIdList.contains("1"), "不允许删除管理员角色");
        // 角色是否已分配
        Long count = userRoleMapper.selectCount(new LambdaQueryWrapper<UserRole>().in(UserRole::getRoleId, roleIdList));
        Assert.isFalse(count > 0, "角色已分配");
        // 删除角色
        roleMapper.deleteByIds(roleIdList);
        // 批量删除角色关联的菜单权限
        roleMenuMapper.deleteRoleMenu(roleIdList);
        // 删除Redis缓存中的菜单权限
        roleIdList.forEach(roleId -> {
            SaSession sessionById = SaSessionCustomUtil.getSessionById("role-" + roleId, false);
            Optional.ofNullable(sessionById).ifPresent(saSession -> saSession.delete("Permission_List"));
        });
    }

    @Override
    public void updateRoleStatus(RoleStatusReq roleStatus) {
        Assert.isFalse(roleStatus.getId().equals("1"), "不允许禁用管理员角色");
        // 更新角色状态
        Role newRole = Role.builder()
                .id(roleStatus.getId())
                .isDisable(roleStatus.getIsDisable())
                .build();
        roleMapper.updateById(newRole);
    }

    @Override
    public List<Integer> listRoleMenuTree(String roleId) {
        return roleMenuMapper.selectMenuByRoleId(roleId);
    }

    @Override
    public void updateRole(RoleReq role) {
        Assert.isFalse(role.getId().equals("1") && role.getIsDisable().equals(1), "不允许禁用管理员角色");
        // 角色名是否存在
        Role existRole = roleMapper.selectOne(new LambdaQueryWrapper<Role>().select(Role::getId).eq(Role::getRoleName, role.getRoleName()));
        Assert.isFalse(Objects.nonNull(existRole) && !existRole.getId().equals(role.getId()),
                role.getRoleName() + "角色名已存在");
        // 更新角色信息
        Role newRole = Role.builder()
                .id(role.getId())
                .roleName(role.getRoleName())
                .roleDesc(role.getRoleDesc()).
                isDisable(role.getIsDisable())
                .build();
        roleMapper.updateById(newRole);
        // 先删除角色关联的菜单权限
        roleMenuMapper.deleteRoleMenuByRoleId(newRole.getId());
        // 再添加角色菜单权限
        roleMenuMapper.insertRoleMenu(newRole.getId(), role.getMenuIdList());
        // 删除Redis缓存中的菜单权限
        SaSession sessionById = SaSessionCustomUtil.getSessionById("role-" + newRole.getId(), false);
        Optional.ofNullable(sessionById).ifPresent(saSession -> saSession.delete("Permission_List"));
    }

    @Override
    @Cache(constants = RedisConstants.USER_ROLE)
    public List<String> getRoleNameByUser(long id) {
        return getRoleByUser(id).stream().map(Role::getRoleName).toList();
    }

    private List<Role> getRoleByUser(long id) {
        List<String> roleIdList = userRoleService.lambdaQuery().select(UserRole::getRoleId)
                .eq(UserRole::getUserId, id).list()
                .stream().map(UserRole::getRoleId).toList();
        if(CollUtil.isEmpty(roleIdList)){
            return Collections.emptyList();
        }
        return lambdaQuery().in(Role::getId,roleIdList).list();
    }

}




