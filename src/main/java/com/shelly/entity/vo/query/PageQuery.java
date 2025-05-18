package com.shelly.entity.vo.query;

import com.shelly.constants.PageConstant;
import lombok.Data;

@Data
public class PageQuery {

    /**
     * 当前页
     */
    private Integer current;

    /**
     * 条数
     */
    private Integer size;

    /**
     * 获取当前页的起始位置。
     * 如果当前页数为 null，则使用默认页数计算。
     * 页数从 0 开始计算。
     *
     * @return 当前页的起始位置
     */
    public Integer getCurrent() {
        int currentPage;

        // 如果 current 为 null，则使用默认页数值为1
        if (current == null) {
            currentPage = PageConstant.DEFAULT_CURRENT;
        } else {
            currentPage = current;
        }

        // 计算当前页的起始位置
        return (currentPage - 1) * getSize();
    }

    /**
     * 获取每页显示的条目数。
     * 如果条目数为 null，则使用默认值。
     *
     * @return 每页显示的条目数
     */
    public Integer getSize() {
        // 如果 size 为 null，则使用默认条目数
        if (size == null) {
            return PageConstant.MY_SIZE;
        } else {
            return size;
        }
    }
    public Integer getOrigPage() {
        return (current == null) ? PageConstant.DEFAULT_CURRENT : current;
    }

}

