package com.atguigu.cloud.controller;

import com.atguigu.cloud.apis.PayFeignApi;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class OrderMicrometerController {

    @Resource
    private PayFeignApi payFeignApi;

    /**
     * 测试链路追踪
     * @param id
     * @return
     */
    @GetMapping("/feign/micrometer/{id}")
    public String myMicrometerTracing(@PathVariable("id") Integer id){
        String result = payFeignApi.myMicrometer(id);
        return result;
    }
}
