package com.shelly.entity.vo.res;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "菜单下拉树Response")
public class MenuTreeResp {

    /**
     * 菜单id
     */
    @ApiModelProperty(value = "菜单id")
    private Integer id;

    /**
     * 父菜单id
     */
    @JsonIgnore
    @ApiModelProperty(value = "父菜单id")
    private Integer parentId;

    /**
     * 菜单名称
     */
    @ApiModelProperty(value = "菜单名称")
    private String label;

    /**
     * 子菜单树
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @ApiModelProperty(value = "子菜单树")
    private List<MenuTreeResp> children;
}