package com.shelly.entity.vo.Query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "说说查询条件")
public class TalkQuery extends PageQuery {

    /**
     * 状态 (1公开  2私密)
     */
    @ApiModelProperty(value = "状态 (1公开  2私密)")
    private Integer status;

}