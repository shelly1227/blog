package com.shelly.interceptor;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shelly.constants.PageConstant;
import com.shelly.utils.PageUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

public class PageableInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        String currentPage = request.getParameter(PageConstant.CURRENT);
        String pageSize = Optional.ofNullable(request.getParameter(PageConstant.SIZE))
                .orElse(PageConstant.DEFAULT_SIZE);
        if (StringUtils.hasText(currentPage)) {
            PageUtils.setCurrentPage(new Page<>(Long.parseLong(currentPage), Long.parseLong(pageSize)));
        }
        return true;
    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) {
        PageUtils.remove();
    }

}