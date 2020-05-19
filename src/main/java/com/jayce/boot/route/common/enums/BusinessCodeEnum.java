package com.jayce.boot.route.common.enums;

import io.swagger.annotations.ApiModel;

@ApiModel(discriminator = "111223", value = "业务码")
public enum BusinessCodeEnum {
    //BusinessCodeEnum
    SUCCESS(1000, "成功"),
    BUSINESS(2000, "业务异常"),
    BUSINESS_VALID(2001, "参数校验失败"),
    FAILED(3000, "失败"),
    ;

    private Integer code;
    private String value;

    BusinessCodeEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static String getValue(Integer code) {
        BusinessCodeEnum[] flagEna = BusinessCodeEnum.values();
        for (BusinessCodeEnum flagEnum : flagEna) {
            if (flagEnum.getCode().equals(code)) {
                return flagEnum.getValue();
            }
        }
        return null;
    }
}
