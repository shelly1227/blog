package com.shelly.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleEnum {
    /**
     * 管理员
     */
    ADMIN("1", "admin"),
    /**
     * 用户
     */
    USER("2", "user"),
    /**
     * 测试账号
     */
    TEST("3", "test");

    /**
     * 角色id
     */
    private final String roleId;

    /**
     * 描述
     */
    private final String name;

}
