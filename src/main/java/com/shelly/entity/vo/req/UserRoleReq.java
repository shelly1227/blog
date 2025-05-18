package com.shelly.entity.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Data
@ApiModel(description = "用户角色Request")
public class UserRoleReq {

    /**
     * 用户id
     */
    @NotNull(message = "用户id不能为空")
    @ApiModelProperty(value = "用户id", required = true)
    private Integer id;

    /**
     * 昵称
     */
    @NotBlank(message = "昵称不能为空")
    @ApiModelProperty(value = "昵称", required = true)
    private String nickname;

    /**
     * 角色id
     */
    @NotEmpty(message = "角色id不能为空")
    @ApiModelProperty(value = "角色id", required = true)
    private List<String> roleIdList;
}