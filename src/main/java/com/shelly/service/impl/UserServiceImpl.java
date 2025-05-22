package com.shelly.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shelly.common.ServiceException;
import com.shelly.constants.CommonConstant;

import com.shelly.entity.pojo.User;
import com.shelly.entity.pojo.UserRole;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.query.OnlineUserQuery;
import com.shelly.entity.vo.query.UserQuery;
import com.shelly.entity.vo.req.*;
import com.shelly.entity.vo.res.*;
import com.shelly.enums.FilePathEnum;
import com.shelly.enums.RedisConstants;
import com.shelly.mapper.MenuMapper;
import com.shelly.mapper.RoleMapper;
import com.shelly.mapper.UserRoleMapper;
import com.shelly.service.UserService;
import com.shelly.mapper.UserMapper;
import com.shelly.strategy.context.UploadStrategyContext;
import com.shelly.utils.RedisUtil;
import com.shelly.utils.SecurityUtils;
import com.shelly.utils.cache.Cache;
import com.shelly.utils.cache.CacheParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

/**
* @author Shelly6
* @description 针对表【t_user】的数据库操作Service实现
* @createDate 2024-07-22 20:24:46
*/
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {
    private final UploadStrategyContext uploadStrategyContext;
    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;
    private final RoleMapper roleMapper;
    private final MenuMapper menuMapper;
    private final RedisUtil redisUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateUserAvatar(MultipartFile file) {
        // 头像上传
        String avatar = uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.AVATAR.getPath());
        // 更新用户头像
        User newUser = User.builder()
                .id(StpUtil.getLoginIdAsInt())
                .avatar(avatar)
                .build();
        userMapper.updateById(newUser);
        return avatar;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserEmail(EmailReq email) {
        verifyCode(email.getEmail(), email.getCode());
        User newUser = User.builder()
                .id(StpUtil.getLoginIdAsInt())
                .email(email.getEmail())
                .build();
        userMapper.updateById(newUser);
    }
    public void verifyCode(String username, String code) {
        String redisKeyPrefix = RedisConstants.VERIFICATION_CODE.getKey();
        String redisKey = redisKeyPrefix + username;
        if (redisUtil.getObject(redisKey) == null || redisUtil.getTime(redisKey) == 0) {
            throw new ServiceException("验证码过期");
        }
        // 验证验证码是否匹配
        if (!code.equals(redisUtil.getObject(redisKey).toString())) {
            throw new ServiceException("验证码错误");
        }
    }
    @Override
    public UserInfoResp getUserInfo() {
        Integer userId = StpUtil.getLoginIdAsInt();
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getNickname, User::getAvatar, User::getUsername, User::getWebSite,
                        User::getIntro, User::getEmail, User::getLoginType)
                .eq(User::getId, userId));
        Set<Object> articleLikeSet = redisUtil.members(RedisConstants.USER_ARTICLE_LIKE.getKey() + userId);
        Set<Object> commentLikeSet = redisUtil.members(RedisConstants.USER_COMMENT_LIKE.getKey() + userId);
        Set<Object> talkLikeSet = redisUtil.members(RedisConstants.USER_TALK_LIKE.getKey() + userId);
        return UserInfoResp
                .builder()
                .id(userId)
                .avatar(user.getAvatar())
                .nickname(user.getNickname())
                .username(user.getUsername())
                .email(user.getEmail())
                .webSite(user.getWebSite())
                .intro(user.getIntro())
                .articleLikeSet(articleLikeSet)
                .commentLikeSet(commentLikeSet)
                .talkLikeSet(talkLikeSet)
                .loginType(user.getLoginType())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserInfo(UserInfoReq userInfo) {
        User newUser = User.builder()
                .id(StpUtil.getLoginIdAsInt())
                .nickname(userInfo.getNickname())
                .intro(userInfo.getIntro())
                .webSite(userInfo.getWebSite())
                .build();
        userMapper.updateById(newUser);
    }

    @Override
    public void updatePassword(UserReq user) {
        verifyCode(user.getUsername(), user.getCode());
        User existUser = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getUsername)
                .eq(User::getUsername, user.getUsername()));
        Assert.notNull(existUser, "邮箱尚未注册！");
        // 根据用户名修改密码
        userMapper.update(new User(), new LambdaUpdateWrapper<User>()
                .set(User::getPassword, SecurityUtils.sha256Encrypt(user.getPassword()))
                .eq(User::getUsername, user.getUsername()));
    }

    @Override
    public UserBackInfoResp getUserBackInfo() {
        Integer userId = StpUtil.getLoginIdAsInt();
        // 查询用户信息
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getAvatar).eq(User::getId, userId));
        // 查询用户角色
        List<String> roleIdList = StpUtil.getRoleList();
        // 用户权限列表
        List<String> permissionList = StpUtil.getPermissionList().stream()
                .filter(string -> !string.isEmpty())
                .distinct()
                .toList();
        return UserBackInfoResp.builder()
                .id(userId)
                .avatar(user.getAvatar())
                .roleList(roleIdList)
                .permissionList(permissionList)
                .build();
    }

    @Override
    @Cache(constants = RedisConstants.USER_MENU)
    public List<RouterResp> getUserMenu(@CacheParam int userId) {
        // 查询用户菜单,只查询出来M，C，不包括B，因为它没有子菜单
        List<UserMenuResp> userMenuRespList = menuMapper.selectMenuByUserId(userId);
        // 递归生成路由,parentId为0
        return recurRoutes(CommonConstant.PARENT_ID, userMenuRespList);
    }
    private List<RouterResp> recurRoutes(Integer parentId, List<UserMenuResp> menuList) {
        List<RouterResp> list = new ArrayList<>();
        Optional.ofNullable(menuList).ifPresent(menus -> menus.stream()
                //过滤出已经不需要递归查找的菜单
                .filter(menu -> menu.getParentId().equals(parentId))
                .forEach(menu -> {
                    RouterResp routeVO = new RouterResp();
                    routeVO.setName(menu.getMenuName());
                    routeVO.setPath(getRouterPath(menu));
                    routeVO.setComponent(getComponent(menu));
                    routeVO.setMeta(MetaResp.builder()
                            .title(menu.getMenuName())
                            .icon(menu.getIcon())
                            .hidden(menu.getIsHidden().equals(CommonConstant.TRUE))
                            .build());
                    if (menu.getMenuType().equals(CommonConstant.TYPE_DIR)) {
                        List<RouterResp> children = recurRoutes(menu.getId(), menuList);
                        if (CollUtil.isNotEmpty(children)) {
                            routeVO.setAlwaysShow(true);
                            routeVO.setRedirect("noRedirect");
                        }
                        routeVO.setChildren(children);
                    } else if (isMenuFrame(menu)) {
                        routeVO.setMeta(null);
                        List<RouterResp> childrenList = new ArrayList<>();
                        RouterResp children = new RouterResp();
                        children.setName(menu.getMenuName());
                        children.setPath(menu.getPath());
                        children.setComponent(menu.getComponent());
                        children.setMeta(MetaResp.builder()
                                .title(menu.getMenuName())
                                .icon(menu.getIcon())
                                .hidden(menu.getIsHidden().equals(CommonConstant.TRUE))
                                .build());
                        childrenList.add(children);
                        routeVO.setChildren(childrenList);
                    }
                    list.add(routeVO);
                }));
        return list;
    }
    public String getComponent(UserMenuResp menu) {
        String component = CommonConstant.LAYOUT;
        if (StringUtils.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu)) {
            component = menu.getComponent();
        } else if (StringUtils.isEmpty(menu.getComponent()) && isParentView(menu)) {
            component = CommonConstant.PARENT_VIEW;
        }
        return component;
    }
    public boolean isParentView(UserMenuResp menu) {
        return !menu.getParentId().equals(CommonConstant.PARENT_ID) && CommonConstant.TYPE_DIR.equals(menu.getMenuType());
    }

    public boolean isMenuFrame(UserMenuResp menu) {
        return menu.getParentId().equals(CommonConstant.PARENT_ID) && CommonConstant.TYPE_MENU.equals(menu.getMenuType());
    }

    public String getRouterPath(UserMenuResp menu) {
        String routerPath = menu.getPath();
        // 一级目录
        if (menu.getParentId().equals(CommonConstant.PARENT_ID) && CommonConstant.TYPE_DIR.equals(menu.getMenuType())) {
            routerPath = "/" + menu.getPath();
        }
        // 一级菜单
        else if (isMenuFrame(menu)) {
            routerPath = "/";
        }
        return routerPath;
    }


    @Override
    public void updateAdminPassword(PasswordReq password) {
        Integer userId = StpUtil.getLoginIdAsInt();
        // 查询旧密码是否正确
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getId, userId));
        Assert.notNull(user, "用户不存在");
        Assert.isTrue(SecurityUtils.checkPw(user.getPassword(), password.getOldPassword()), "旧密码校验不通过!");
        // 正确则修改密码
        String newPassword = SecurityUtils.sha256Encrypt(password.getNewPassword());
        user.setPassword(newPassword);
        userMapper.updateById(user);
    }

    @Override
    public void updateUserStatus(DisableReq disable) {
        // 更新用户状态
        User newUser = User.builder()
                .id(disable.getId())
                .isDisable(disable.getIsDisable())
                .build();
        userMapper.updateById(newUser);
        if (disable.getIsDisable().equals(CommonConstant.TRUE)) {
            // 先踢下线
            StpUtil.logout(disable.getId());
            // 再封禁账号
            StpUtil.disable(disable.getId(), 86400);
        } else {
            // 解除封禁
            StpUtil.untieDisable(disable.getId());
        }
    }

    @Override
    public void updateUser(UserRoleReq user) {
        // 更新用户信息
        User newUser = User.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .build();
        baseMapper.updateById(newUser);
        // 删除用户角色
        userRoleMapper.delete(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, user.getId()));
        // 重新添加用户角色
        userRoleMapper.insertUserRole(user.getId(), user.getRoleIdList());
        // 删除Redis缓存中的角色
        //redisUtil.remove(RedisConstants.USER + user.getId().toString());
        redisUtil.remove(RedisConstants.USER_INFO + user.getId().toString());
    }

    @Override
    public void kickOutUser(String token) {
        StpUtil.logoutByTokenValue(token);
        redisUtil.remove(RedisConstants.USER_TOKEN + token);
    }

    @Override
    public PageResult<OnlineUserResp> listOnlineUser(OnlineUserQuery onlineUserQuery) {
        // 查询所有token及对应的userId
        Map<Object, Object> allTokenMap = redisUtil.getHashAll(RedisConstants.USER_TOKEN.getKey());
        Set<Object> userIdSet = allTokenMap.keySet();

        List<OnlineUserResp> onlineUserRespList = userIdSet.stream()
                .map(userId -> {
                    return redisUtil.get(RedisConstants.USER_INFO.getKey() + userId.toString(), OnlineUserResp.class);
                })
                .filter(Objects::nonNull)
                .filter(onlineUserResp -> StringUtils.isEmpty(onlineUserQuery.getKeyword())
                        || onlineUserResp.getNickname().contains(onlineUserQuery.getKeyword()))
                .sorted(Comparator.comparing(OnlineUserResp::getLoginTime).reversed())
                .toList();
        int fromIndex = onlineUserQuery.getCurrent();
        int size = onlineUserQuery.getSize();
        int toIndex = onlineUserRespList.size() - fromIndex > size ? fromIndex + size : onlineUserRespList.size();
        List<OnlineUserResp> userOnlineList = fromIndex >= onlineUserRespList.size() ?
                Collections.emptyList() : onlineUserRespList.subList(fromIndex, toIndex);
        return new PageResult<>(userOnlineList, (long) onlineUserRespList.size());
    }

    @Override
    public PageResult<UserBackResp> listUserBackVO(UserQuery userQuery) {
        // 查询后台用户数量
        Long count = userMapper.selectUserCount(userQuery);
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询后台用户列表
        List<UserBackResp> userBackRespList = userMapper.selectUserList(userQuery);
        return new PageResult<>(userBackRespList, count);
    }

    @Override
    public List<UserRoleResp> listUserRoleDTO() {
        // 查询角色列表
        return roleMapper.selectUserRoleList();
    }
}




