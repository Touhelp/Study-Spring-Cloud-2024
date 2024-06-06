package com.atguigu.cloud.controller;

import com.atguigu.cloud.service.FlowLimitService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class FlowLimitController
{

    @GetMapping("/testA")
    public String testA()
    {
        log.info("Jmeter test...");
        return "------testA";
    }

    @GetMapping("/testB")
    public String testB()
    {
        log.info("jmeter test...");
        return "------testB";
    }


    /**
     * testC\testD方法用于测试Sentinel针对不同链路调用资源的限流策略
     */

    @Resource
    private FlowLimitService flowLimitService;

    @GetMapping("/testC")
    public String testC()
    {
        flowLimitService.common();
        return "------testC";
    }

    @GetMapping("/testD")
    public String testD()
    {
        flowLimitService.common();
        return "------testD";
    }


    /**
     * 测试流控效果-排队等待
     * @return
     */
    @GetMapping("/testE")
    public String testE(){
        System.out.println(System.currentTimeMillis() + "  testE......");
        return "------testE";
    }

    /**
     * 服务熔断rule-慢调用比例
     * @return
     */
    @GetMapping("/testF")
    public String testF(){
        //暂停几秒钟线程
        try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
        System.out.println("----测试:新增熔断规则-慢调用比例 ");
        return "------testF 新增熔断规则-慢调用比例";
    }

    /**
     * 新增熔断规则-异常比例
     * @return
     */
    @GetMapping("/testG")
    public String testG()
    {
        System.out.println("----测试:新增熔断规则-异常比例 ");
        int age = 10/0;
        return "------testG,新增熔断规则-异常比例 ";
    }







}
