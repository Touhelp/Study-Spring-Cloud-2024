package com.atguigu.cloud.service;

import com.atguigu.cloud.entities.Pay;

import java.util.List;

public interface PayService {

    Integer add(Pay pay);

    Integer delete(Integer id);

    Integer update(Pay pay);

    Pay getById(Integer id);

    List<Pay> getAll();

}
