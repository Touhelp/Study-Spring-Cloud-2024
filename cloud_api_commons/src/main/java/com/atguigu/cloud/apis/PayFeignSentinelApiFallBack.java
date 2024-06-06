package com.atguigu.cloud.apis;

import com.atguigu.cloud.resp.ResultData;
import com.atguigu.cloud.resp.ReturnCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PayFeignSentinelApiFallBack implements PayFeignSentinelApi{
    @Override
    public ResultData getPayByOrderNo(String orderNo) {
        log.info("Entering PayFeignSentinelApiFallBack class ......");
        return ResultData.fail(ReturnCodeEnum.RC500.getCode(),"对方服务宕机或不可用，FallBack服务降级o(╥﹏╥)o");
    }
}
