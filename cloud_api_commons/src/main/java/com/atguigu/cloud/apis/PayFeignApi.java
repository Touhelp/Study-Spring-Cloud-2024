package com.atguigu.cloud.apis;

import cn.hutool.core.util.IdUtil;
import com.atguigu.cloud.entities.PayDTO;
import com.atguigu.cloud.resp.ResultData;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@FeignClient(name = "cloud-payment-service")
@FeignClient(name = "cloud-gateway")
public interface PayFeignApi {

    /**
     * 新增支付流水方法,json串做参数
     * @param payDTO
     * @return
     */
    @PostMapping("/pay/add")
    public ResultData addPay(@RequestBody PayDTO payDTO);

    /**
     * 修改支付流水方法
     * @param payDTO
     * @return
     */
    @PutMapping("/pay/update")
    public ResultData updatePay(@RequestBody PayDTO payDTO);

    /**
     * 删除支付流水方法
     * @param id
     * @return
     */
    @DeleteMapping("/pay/del/{id}")
    public ResultData deletePay(@PathVariable("id") Integer id);

    /**
     * 查询支付流水方法
     * @param id
     * @return
     */
    @GetMapping("/pay/get/{id}")
    public ResultData selectByIdPay(@PathVariable("id") Integer id);

    /**
     * 测试负载均衡
      * @return
     */
    @GetMapping("/pay/get/info")
    public ResultData<String> getInfoByConsul();


    //=========Resilience4j CircuitBreaker 的例子
    /**
     * 用于测试熔断器
     */
    @GetMapping(value = "/pay/circuit/{id}")
    public String myCircuit(@PathVariable("id") Integer id);


    //=========Resilience4j bulkhead 的例子
    @GetMapping(value = "/pay/bulkhead/{id}")
    public String myBulkhead(@PathVariable("id") Integer id);

    //=========Resilience4j ratelimit 的例子
    @GetMapping(value = "/pay/ratelimit/{id}")
    public String myRatelimit(@PathVariable("id") Integer id);

    /**
     * Micrometer(Sleuth)进行链路监控的例子
     * @param id
     * @return
     */
    @GetMapping(value = "/pay/micrometer/{id}")
    public String myMicrometer(@PathVariable("id") Integer id);


    /**
     * GateWay进行网关测试案例01
     * @param id
     * @return
     */
    @GetMapping(value = "/pay/gateway/get/{id}")
    public ResultData getById(@PathVariable("id") Integer id);

    /**
     * GateWay进行网关测试案例02
     * @return
     */
    @GetMapping(value = "/pay/gateway/info")
    public ResultData<String> getGatewayInfo();

}
