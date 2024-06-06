package com.atguigu.cloud.controller;

import com.atguigu.cloud.entities.PayDTO;
import com.atguigu.cloud.resp.ResultData;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/consumer")
@Slf4j
public class OrderController {

    //public static final String PaymentSrv_URL = "http://localhost:8001/pay";
    public static final String PaymentSrv_URL = "http://cloud-payment-service"; //IP地址和端口号修改为在Consul配置的名字

    @Resource
    private RestTemplate restTemplate;

    @GetMapping("/get/{id}")
    public ResultData getPayInfoById(@PathVariable("id") Integer id){
        return restTemplate.getForObject(PaymentSrv_URL + "/pay/get/" + id, ResultData.class);
    }

    @GetMapping("/get")
    public ResultData getPayAllInfo(){
        ResponseEntity<ResultData> response = restTemplate.getForEntity(PaymentSrv_URL + "/pay/get", ResultData.class);
        log.info("Reader: {}", response.getHeaders());
        log.info("StatusCode: {}", response.getStatusCode());
        log.info("Body: {}", response.getBody());
        return response.getBody();
    }

    @DeleteMapping("/delete/{id}")
    public ResultData deleteOrder(@PathVariable("id") Integer id){
        restTemplate.delete(PaymentSrv_URL + "/pay/del/" + id);
        return ResultData.success();
    }

    @PutMapping("/update")
    public ResultData updateOrder(@RequestBody PayDTO payDTO){
        restTemplate.put(PaymentSrv_URL + "/pay/update", payDTO);
        return ResultData.success();
    }

    @PostMapping("/add")
    public ResultData addOrder(@RequestBody PayDTO payDTO){
        ResultData resultData = restTemplate.postForObject(PaymentSrv_URL + "/pay/add", payDTO, ResultData.class);
        return resultData;
    }

    @GetMapping("/get/info")
    public ResultData getInfo(){
        return restTemplate.getForObject(PaymentSrv_URL + "/pay/get/info", ResultData.class);
    }

    /*@GetMapping("/get/info")
    public String getInfo(){
        return restTemplate.getForObject(PaymentSrv_URL + "/pay/get/info", String.class);
    }*/


    @Resource
    private DiscoveryClient discoveryClient;
    @GetMapping("/consumer/discovery")
    public String discovery()
    {
        List<String> services = discoveryClient.getServices();
        for (String element : services) {
            System.out.println(element);
        }

        System.out.println("===================================");

        List<ServiceInstance> instances = discoveryClient.getInstances("cloud-payment-service");
        for (ServiceInstance element : instances) {
            System.out.println(element.getServiceId()+"\t"+element.getHost()+"\t"+element.getPort()+"\t"+element.getUri());
        }

        return instances.get(0).getServiceId()+":"+instances.get(0).getPort();
    }



}
