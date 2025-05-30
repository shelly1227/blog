package com.shelly.entity.vo.res;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class TagBackResp extends TagResp {
    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}