package com.shelly.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.shelly.entity.pojo.Menu;
import com.shelly.entity.pojo.RoleMenu;
import com.shelly.entity.vo.Query.MenuQuery;
import com.shelly.entity.vo.Request.MenuReq;
import com.shelly.entity.vo.Response.MenuOptionResp;
import com.shelly.entity.vo.Response.MenuResp;
import com.shelly.entity.vo.Response.MenuTreeResp;
import com.shelly.mapper.RoleMenuMapper;
import com.shelly.service.MenuService;
import com.shelly.mapper.MenuMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author Shelly6
* @description 针对表【t_menu】的数据库操作Service实现
* @createDate 2024-07-22 20:24:46
*/
@Service
@RequiredArgsConstructor
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu>
    implements MenuService{
    private final MenuMapper menuMapper;
    private final RoleMenuMapper roleMenuMapper;

    @Override
    public void addMenu(MenuReq menu) {
        // 名称是否存在
        Menu existMenu = menuMapper.selectOne(new LambdaQueryWrapper<Menu>()
                .select(Menu::getId)
                .eq(Menu::getMenuName, menu.getMenuName()));
        Assert.isNull(existMenu, menu.getMenuName() + "菜单已存在");
        Menu newMenu = new Menu();
        BeanUtils.copyProperties(menu, newMenu);
        baseMapper.insert(newMenu);
    }

    @Override
    public void deleteMenu(Integer menuId) {
        // 菜单下是否存在子菜单
        Long menuCount = menuMapper.selectCount(new LambdaQueryWrapper<Menu>()
                .eq(Menu::getParentId, menuId));
        Assert.isFalse(menuCount > 0, "存在子菜单");
        // 菜单是否已分配，即是否有角色拥有该权限
        Long roleCount = roleMenuMapper.selectCount(new LambdaQueryWrapper<RoleMenu>()
                .eq(RoleMenu::getMenuId, menuId));
        Assert.isFalse(roleCount > 0, "菜单已分配");
        // 删除菜单
        menuMapper.deleteById(menuId);
    }

    @Override
    public MenuReq editMenu(Integer menuId) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Menu::getId, Menu::getParentId, Menu::getMenuType, Menu::getMenuName,
                        Menu::getPath, Menu::getIcon, Menu::getComponent, Menu::getPerms,
                        Menu::getIsHidden, Menu::getIsDisable, Menu::getOrderNum)
                .eq(Menu::getId, menuId);
        Menu menu = menuMapper.selectOne(queryWrapper);
        MenuReq menuReq = new MenuReq();
        BeanUtils.copyProperties(menu, menuReq);
        return menuReq;
    }
//TODO
    @Override
    public List<MenuOptionResp> listMenuOption() {
        // 获取菜单树
        List<MenuOptionResp> menuOptionList = menuMapper.selectMenuOptions();
        return recurMenuOptionList(0, menuOptionList);
    }

    @Override
    public List<MenuTreeResp> listMenuTree() {
        List<MenuTreeResp> menuTreeRespList = menuMapper.selectMenuTree();
        return recurMenuTreeList(0, menuTreeRespList);
    }

    @Override
    public List<MenuResp> listMenuVO(MenuQuery menuQuery) {
        // 查询当前菜单列表
        List<MenuResp> menuRespList = menuMapper.selectMenuVOList(menuQuery);
        // 当前菜单id列表
        Set<Integer> menuIdList = menuRespList.stream()
                .map(MenuResp::getId)
                .collect(Collectors.toSet());
        return menuRespList.stream().map(menuVO -> {
            Integer parentId = menuVO.getParentId();
            // parentId不在当前菜单id列表，说明为父级菜单id，根据此id作为递归的开始条件节点
            if (!menuIdList.contains(parentId)) {
                menuIdList.add(parentId);
                return recurMenuList(parentId, menuRespList);
            }
            return new ArrayList<MenuResp>();
        }).collect(ArrayList::new, ArrayList::addAll, ArrayList::addAll);
    }

    @Override
    public void updateMenu(MenuReq menu) {
        // 名称是否存在
        Menu existMenu = menuMapper.selectOne(new LambdaQueryWrapper<Menu>()
                .select(Menu::getId)
                .eq(Menu::getMenuName, menu.getMenuName()));
        Assert.isFalse(Objects.nonNull(existMenu) && !existMenu.getId().equals(menu.getId()),
                menu.getMenuName() + "菜单已存在");
        Menu newMenu = new Menu();
        BeanUtils.copyProperties(menu, newMenu);
        baseMapper.updateById(newMenu);
    }

    private List<MenuResp> recurMenuList(Integer parentId, List<MenuResp> menuList) {
        return menuList.stream()
                .filter(menuVO -> menuVO.getParentId().equals(parentId))
                .peek(menuVO -> menuVO.setChildren(recurMenuList(menuVO.getId(), menuList)))
                .collect(Collectors.toList());
    }
    private List<MenuOptionResp> recurMenuOptionList(Integer parentId, List<MenuOptionResp> menuOptionList) {
        return menuOptionList.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .peek(menu -> menu.setChildren(recurMenuOptionList(menu.getValue(), menuOptionList)))
                .collect(Collectors.toList());
    }

    private List<MenuTreeResp> recurMenuTreeList(Integer parentId, List<MenuTreeResp> menuTreeRespList) {
        return menuTreeRespList.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .peek(menu -> menu.setChildren(recurMenuTreeList(menu.getId(), menuTreeRespList)))
                .collect(Collectors.toList());
    }
}




