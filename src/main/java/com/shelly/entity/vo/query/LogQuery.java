package com.shelly.entity.vo.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LogQuery extends PageQuery{
    private String keyword;

    private String optModule;
}
