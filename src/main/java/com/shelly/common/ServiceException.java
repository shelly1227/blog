package com.shelly.common;

import lombok.Getter;

import static com.shelly.enums.StatusCodeEnum.FAIL;


@Getter
public final class ServiceException extends RuntimeException {

    /**
     * 返回失败状态码
     */
    private final Integer code = FAIL.getCode();

    /**
     * 返回信息
     */
    private final String message;

    public ServiceException(String message) {
        this.message = message;
    }

}