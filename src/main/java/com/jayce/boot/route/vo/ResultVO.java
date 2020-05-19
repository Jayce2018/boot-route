package com.jayce.boot.route.vo;

import com.jayce.boot.route.common.enums.BusinessCodeEnum;
import io.swagger.annotations.ApiModel;
import lombok.Getter;

@ApiModel(discriminator = "jayce", value = "统一返回对象")
@Getter
public class ResultVO<T> {
    /**
     * 状态码，比如1000代表响应成功
     */
    private int code;
    /**
     * 响应信息，用来说明响应情况
     */
    private String msg;
    /**
     * 响应的具体数据
     */
    private T data;

    public ResultVO(T data) {
        this(BusinessCodeEnum.SUCCESS.getCode(), "success", data);
    }

    public ResultVO(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResultVO(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}