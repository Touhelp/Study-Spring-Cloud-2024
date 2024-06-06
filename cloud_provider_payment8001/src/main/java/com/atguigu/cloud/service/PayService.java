package com.atguigu.cloud.service;

import com.atguigu.cloud.entities.Pay;
import com.atguigu.cloud.entities.PayDTO;

import java.util.List;

public interface PayService {

    Integer add(PayDTO payDTO);

    Integer delete(Integer id);

    Integer update(Pay pay);

    Pay getById(Integer id);

    List<Pay> getAll();

}
