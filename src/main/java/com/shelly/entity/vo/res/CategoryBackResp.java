package com.shelly.entity.vo.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class CategoryBackResp extends CategoryResp {

    /**
     * 父级ID
     */
    private Integer parentId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 子分类列表
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<CategoryBackResp> children;

}