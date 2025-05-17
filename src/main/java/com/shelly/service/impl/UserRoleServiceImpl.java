package com.shelly.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.shelly.entity.pojo.UserRole;
import com.shelly.service.UserRoleService;
import com.shelly.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

/**
* @author Shelly6
* @description 针对表【t_user_role】的数据库操作Service实现
* @createDate 2024-07-22 20:24:46
*/
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
    implements UserRoleService {

}




