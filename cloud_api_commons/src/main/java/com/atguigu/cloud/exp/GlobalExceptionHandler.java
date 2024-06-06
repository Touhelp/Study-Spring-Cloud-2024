package com.atguigu.cloud.exp;

import com.atguigu.cloud.resp.ResultData;
import com.atguigu.cloud.resp.ReturnCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
//@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //手动设置该方法被调用时返回的状态码，而不是使用默认的状态码
    public ResultData<String> getGlobalExceptionHand(RuntimeException e){

        ResultData<String> resultData = new ResultData<>();

        resultData.setCode(ReturnCodeEnum.RC500.getCode());
        resultData.setMessage(e.getMessage());
        resultData.setData("服务器内部故障！");

        return resultData;

    }


}
