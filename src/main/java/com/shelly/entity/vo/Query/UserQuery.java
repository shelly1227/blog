package com.shelly.entity.vo.Query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "用户查询条件")
public class UserQuery extends PageQuery {

    /**
     * 搜索内容
     */
    @ApiModelProperty(value = "搜索内容")
    private String keyword;

    /**
     * 登录方式 (1邮箱 2QQ 3Gitee 4Github)
     */
    @ApiModelProperty(value = "登录方式 (1邮箱 2QQ 3Gitee 4Github)")
    private Integer loginType;

}