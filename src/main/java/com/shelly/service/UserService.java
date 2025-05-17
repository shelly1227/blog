package com.shelly.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.shelly.entity.pojo.User;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.Query.OnlineUserQuery;
import com.shelly.entity.vo.Query.UserQuery;
import com.shelly.entity.vo.Request.*;
import com.shelly.entity.vo.Response.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
* @author Shelly6
* @description 针对表【t_user】的数据库操作Service
* @createDate 2024-07-22 20:24:46
*/
public interface UserService extends IService<User> {

    String updateUserAvatar(MultipartFile file);

    void updateUserEmail(EmailReq email);

    UserInfoResp getUserInfo();

    void updateUserInfo(UserInfoReq userInfo);

    void updatePassword(UserReq user);

    UserBackInfoResp getUserBackInfo();

    List<RouterResp> getUserMenu();

    void updateAdminPassword(PasswordReq password);

    void updateUserStatus(DisableReq disable);

    void updateUser(UserRoleReq user);

    void kickOutUser(String token);

    PageResult<OnlineUserResp> listOnlineUser(OnlineUserQuery onlineUserQuery);

    PageResult<UserBackResp> listUserBackVO(UserQuery userQuery);

    List<UserRoleResp> listUserRoleDTO();
}
