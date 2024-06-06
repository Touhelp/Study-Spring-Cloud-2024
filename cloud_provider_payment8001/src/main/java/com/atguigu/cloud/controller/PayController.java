package com.atguigu.cloud.controller;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.atguigu.cloud.entities.Pay;
import com.atguigu.cloud.entities.PayDTO;
import com.atguigu.cloud.resp.ResultData;
import com.atguigu.cloud.service.PayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
@Tag(name = "支付微服务模块",description = "支付CRUD")
public class PayController {

    @Resource
    private PayService payService;

    @Operation(summary = "新增",description = "新增支付流水方法,json串做参数")
    @PostMapping("/pay/add")
    public ResultData addPay(@RequestBody PayDTO payDTO){
        log.info("开始添加订单");
        payService.add(payDTO);
        return ResultData.success();
    }


    @Operation(summary = "修改", description = "修改支付流水方法")
    @PutMapping("/pay/update")
    public ResultData updatePay(@RequestBody PayDTO payDTO){
        Pay pay = new Pay();
        //拷贝属性
        BeanUtils.copyProperties(payDTO, pay);
        payService.update(pay);
        return ResultData.success();
    }


    @Operation(summary = "删除", description = "删除支付流水方法")
    @DeleteMapping("/pay/del/{id}")
    public ResultData deletePay(@PathVariable("id") Integer id){
        payService.delete(id);
        return ResultData.success();
    }

    @Operation(summary = "按照ID查流水",description = "查询支付流水方法")
    @GetMapping("/pay/get/{id}")
    public ResultData<Pay> selectByIdPay(@PathVariable("id") Integer id){
        Pay pay = payService.getById(id);
//        int i = 10 / 0;
        log.info("进入selectByIdPay()......");
        try {
            TimeUnit.SECONDS.sleep(63);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return ResultData.success(pay);
    }

    @Operation(summary = "查询所有支付流水", description = "查询所有支付流水方法")
    @GetMapping("/pay/get")
    public ResultData<List<Pay>> selectAllPay(){
        List<Pay> list = payService.getAll();
        return ResultData.success(list);
    }

    @Value("${server.port}")
    private String port;

    @GetMapping("/pay/get/info")
    public ResultData<String> getInfoByConsul(@Value("${test.info}") String info){
        String str =  "test.info: " + info + " " + port;
        return ResultData.success(str);
    }

    /*@GetMapping("/get/info")
    public String getInfoByConsul(@Value("${test.info}") String info){
        String str =  "test.info: " + info + " " + port;
        return str;
    }*/

}
