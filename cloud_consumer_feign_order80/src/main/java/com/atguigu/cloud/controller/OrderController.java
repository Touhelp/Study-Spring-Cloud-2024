package com.atguigu.cloud.controller;

import cn.hutool.core.date.DateUtil;
import com.atguigu.cloud.apis.PayFeignApi;
import com.atguigu.cloud.entities.PayDTO;
import com.atguigu.cloud.resp.ResultData;
import com.atguigu.cloud.resp.ReturnCodeEnum;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/openfeign/consumer")
@Slf4j
public class OrderController {

    @Resource
    private PayFeignApi payFeignApi;

    /**
     * 新增支付流水方法,json串做参数
     */
    @PostMapping("/openfeign/consumer/add")
    public ResultData addPay(@RequestBody PayDTO payDTO){
        log.info("生成订单，调用支付模块");
        ResultData resultData = payFeignApi.addPay(payDTO);
        return resultData;
    }


    /**
     * 删除支付流水方法
     */
    @DeleteMapping("/openfeign/consumer/del/{id}")
    public ResultData deletePay(@PathVariable("id") Integer id){
        ResultData resultData = payFeignApi.deletePay(id);
        return resultData;
    }

    /**
     * 查询支付流水方法
     * @param id
     * @return
     */
    @GetMapping("/openfeign/consumer/get/{id}")
    public ResultData selectByIdPay(@PathVariable("id") Integer id){
        log.info("order-id::{}", id);
        ResultData resultData = null;

        //设置睡眠时间，来测试OpenFeign的超时控制
        try {
            System.out.println("Request is coming...... " + DateUtil.now());
            resultData = payFeignApi.selectByIdPay(id);
        } catch (Exception e) {
            System.out.println("Request is over ...... " + DateUtil.now());
            e.printStackTrace();
            return ResultData.fail(ReturnCodeEnum.RC500.getCode(), "请求超时");
        }

        return resultData;

    }

    /**
     * 测试负载均衡
     */
    @GetMapping("/openfeign/consumer/get/info")
    public ResultData<String> getInfoByConsul(){
        ResultData<String> infoByConsul = payFeignApi.getInfoByConsul();
        return infoByConsul;
    }


    /**
     * 测试熔断器
     * @param id
     * @return
     */
    @CircuitBreaker(name = "cloud-payment-service", fallbackMethod = "myCircuitFallBack")
    @GetMapping("/openfeign/consumer/get/circuit/{id}")
    public String testCircuitBreaker(@PathVariable("id") Integer id){
        String result = payFeignApi.myCircuit(id);
        return result;
    }

    public String myCircuitFallBack(Integer id, Throwable t){
        // 这里是容错处理逻辑，返回备用结果
        return "myCircuitFallback，系统繁忙，请稍后再试-----/(ㄒoㄒ)/~~";
    }

    /**
     * 测试舱壁-限制并发的数量-使用信号量机制
     * @return
     *//*
    @GetMapping("/openfeign/consumer/get/bulkhead/{id}")
    @Bulkhead(name = "cloud-payment-service", fallbackMethod = "myBulkHeadFallBack", type = Bulkhead.Type.SEMAPHORE)
    public String myBulkHead(@PathVariable("id") Integer id){
        String result = payFeignApi.myBulkhead(id);
        return result;
    }

    *//**
     * 注意Throwble这个形参一定要配，不然会找不到fallbackMethod
     * @param t
     * @return
     *//*
    public String myBulkHeadFallBack(Throwable t){
        // 这里是容错处理逻辑，返回备用结果
        return "myBulkheadFallback，隔板超出最大数量限制，请稍后再试-----/(ㄒoㄒ)/~~";
    }
*/

    /**
     * 测试舱壁-限制并发的数量-使用信号量机制
     * @return
     */
    @GetMapping("/openfeign/consumer/get/bulkhead/{id}")
    @Bulkhead(name = "cloud-payment-service", fallbackMethod = "myBulkHeadFallBackTHREADPOOL", type = Bulkhead.Type.THREADPOOL)
    public CompletableFuture<String> myBulkHead(@PathVariable("id") Integer id){

        System.out.println(Thread.currentThread().getName() + "\t" + "enter the method!!!");
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "\t" + "exist the method!!!");

        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() ->
                payFeignApi.myBulkhead(id) + "\t" + " Bulkhead.Type.THREADPOOL"
        );

        return completableFuture;
    }

    /**
     * 注意Throwble这个形参一定要配，不然会找不到fallbackMethod
     //     * @param id
     * @param t
     * @return
     */
    public CompletableFuture<String> myBulkHeadFallBackTHREADPOOL(Throwable t){
        // 这里是容错处理逻辑，返回备用结果
        return CompletableFuture.supplyAsync(() -> "Bulkhead.Type.THREADPOOL，系统繁忙，请稍后再试-----/(ㄒoㄒ)/~~");
    }


    @GetMapping("/openfeign/consumer/get/ratelimiter/{id}")
    @RateLimiter(name = "cloud-payment-service", fallbackMethod = "myFallbackMethodRateLimiter")
    public String myRateLimiter(@PathVariable("id") Integer id){
        return payFeignApi.myRatelimit(id);
    }

    public String myFallbackMethodRateLimiter(Integer id, Throwable t){
        return "你被限流了，禁止访问/(ㄒoㄒ)/~~";
    }




}
