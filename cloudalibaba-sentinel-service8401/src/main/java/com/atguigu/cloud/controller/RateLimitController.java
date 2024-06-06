package com.atguigu.cloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class RateLimitController {

    /**
     * 1. 不使用@SentinelResource注解，采用默认的限流方式
     * @return
     */
    @GetMapping("/rateLimit/byUrl")
    public String testByUrl(){
        return "testByUrl......rest地址限流测试";
    }

    /**
     * 测试SentinelResource注解
     * @return
     */
    @GetMapping("/rateLimit/byResource")
    @SentinelResource(value = "resource01", blockHandler = "handlerBlockHandler01")
    public String testByBlockHandler(){
        return "Entering the testByBlockHandler!";
    }

    public String handlerBlockHandler01(BlockException exception){
        return "自定义服务限流......";
    }


    @GetMapping("/rateLimit/byFallback/{id}")
    @SentinelResource(value = "resource02", blockHandler = "handlerBlockHandler02", fallback = "myFallback")
    public String handlerBlockHandler(@PathVariable("id") Integer id){

        //测试抛出异常
        if (id == 0){
            throw new RuntimeException("runtimeException ......");
        }

        return "Entering the handlerBlockHandler!";

    }

    public String handlerBlockHandler02(@PathVariable("id") Integer id, BlockException blockException){
        return "自定义服务限流......， 触发blockHandler方法";
    }

    public String myFallback(@PathVariable("id") Integer id, Throwable t){
        return "业务逻辑发生异常，触发fallback兜底方法......";
    }


    /**
     * 测试Sentinel中的热点规则
     */
    @GetMapping("/rateLimit/hotkey")
    @SentinelResource(value = "resourceToHotkey", blockHandler = "hotkeyBlockHandler")
    public String testHotParams(
            @RequestParam(value = "key1", required = false) Integer key1,
            @RequestParam(value = "key2", required = false) Integer key2
    ){
        return "Entering testHotParams method about hotkey";
    }


    public String hotkeyBlockHandler(@RequestParam(value = "key1", required = false) Integer key1,
                                     @RequestParam(value = "key2", required = false) Integer key2,
                                     BlockException blockException
                                     ){
        return "Entering current-limiting method!";
    }









}
