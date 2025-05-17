package com.shelly.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shelly.entity.pojo.User;
import com.shelly.entity.vo.Query.UserQuery;
import com.shelly.entity.vo.Response.UserBackResp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Shelly6
* @description 针对表【t_user】的数据库操作Mapper
* @createDate 2024-07-22 20:24:46
* @Entity generator.entity.User
*/
public interface UserMapper extends BaseMapper<User> {

    Long selectUserCount(@Param("param")UserQuery userQuery);

    List<UserBackResp> selectUserList(@Param("param")UserQuery userQuery);
}




