package com.jayce.boot.route.common.exception;

import com.jayce.boot.route.common.enums.BusinessCodeEnum;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{
    private int code;

    private String message;

    private Object data;

    public BusinessException(BusinessCodeEnum item) {
        this.code = item.getCode();
        this.message = item.getValue();
    }

    public BusinessException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public BusinessException(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
