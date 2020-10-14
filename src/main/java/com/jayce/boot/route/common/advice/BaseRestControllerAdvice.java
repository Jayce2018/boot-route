package com.jayce.boot.route.common.advice;

import com.jayce.boot.route.common.enums.BusinessCodeEnum;
import com.jayce.boot.route.common.exception.BusinessException;
import com.jayce.boot.route.vo.ResultVO;
import io.swagger.annotations.ApiModel;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@ApiModel(discriminator = "jayce", value = "全局异常处理")
@RestControllerAdvice
public class BaseRestControllerAdvice {
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultVO businessException(BusinessException e) {
        e.printStackTrace();
        return new ResultVO(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultVO methodArgumentNotValidException(MethodArgumentNotValidException e) {
        // 从异常对象中拿到ObjectError对象
        ObjectError objectError = e.getBindingResult().getAllErrors().get(0);
        return new ResultVO(BusinessCodeEnum.BUSINESS_VALID.getCode(), objectError.getDefaultMessage());
    }


}
