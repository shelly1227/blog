package com.shelly.entity.vo.res;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ArticleConditionList {

    /**
     * 文章列表
     */

    private List<ArticleConditionResp> articleConditionVOList;

    /**
     * 条件名
     */

    private String name;
}