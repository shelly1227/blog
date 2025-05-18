package com.shelly.entity.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "逻辑删除")
public class DeleteReq {

    /**
     * id列表
     */
    @NotEmpty(message = "id不能为空")
    @ApiModelProperty(value = "id列表", required = true)
    private List<Integer> idList;

    /**
     * 是否删除 (0否 1是)
     */
    @NotNull(message = "状态值不能为空")
    @ApiModelProperty(value = "是否删除 (0否 1是)", required = true)
    private Integer isDelete;
}
