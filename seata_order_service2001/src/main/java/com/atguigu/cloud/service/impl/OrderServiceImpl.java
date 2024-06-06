package com.atguigu.cloud.service.impl;

import com.atguigu.cloud.apis.AccountFeignApi;
import com.atguigu.cloud.apis.StorageFeignApi;
import com.atguigu.cloud.entities.Order;
import com.atguigu.cloud.mapper.OrderMapper;
import com.atguigu.cloud.service.OrderService;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource //订单微服务通过OpenFeign去调用账户微服务
    private AccountFeignApi accountFeignApi;

    @Resource //订单微服务通过OpenFeign去调用库存微服务
    private StorageFeignApi storageFeignApi;

    @Override
    @GlobalTransactional(name = "aimin-global-transaction", rollbackFor = Exception.class)
    public void create(Order order) {
        //xid检查
        String xid = RootContext.getXID();

        //1. 新建订单
        log.info("==================>开始新建订单"+"\t"+"xid_order:" +xid);

        //1. 更新订单状态为0, 0：创建中；1：已完结
        order.setStatus(0);

        //2. 插入订单
        int i = orderMapper.insertSelective(order);
        System.out.println(order);

        //插入订单成功后获得插入mysql的实体对象
        Order orderFromDB = null;

        if (i > 0){
            //重新查询订单,获取订单id
            orderFromDB = orderMapper.selectOne(order);
            //orderFromDB = orderMapper.selectByPrimaryKey(order.getId());
            log.info("-------> 新建订单成功，orderFromDB info: "+orderFromDB);

            //3. 修改库存
            log.info("-------> 订单微服务开始调用Storage库存，做扣减count");
            storageFeignApi.decrease(orderFromDB.getProductId(), orderFromDB.getCount());
            log.info("-------> 订单微服务结束调用Storage库存，做扣减完成");
            System.out.println();

            //4. 修改用户金额
            log.info("-------> 订单微服务开始调用Account账号，做扣减money");
            accountFeignApi.decrease(orderFromDB.getUserId(), orderFromDB.getMoney());
            log.info("-------> 订单微服务结束调用Account账号，做扣减完成");
            System.out.println();

            //5. 修改订单状态
            //订单状态status：0：创建中；1：已完结
            log.info("-------> 修改订单状态");
            orderFromDB.setStatus(1);

            Example example = new Example(Order.class);
            Example.Criteria criteria = example.createCriteria();

            criteria.andEqualTo("userId",orderFromDB.getUserId());
            criteria.andEqualTo("status",0);

            int updateResult = orderMapper.updateByExampleSelective(orderFromDB, example);

            log.info("-------> 修改订单状态完成"+"\t"+updateResult);
            log.info("-------> orderFromDB info: "+orderFromDB);

        }

        System.out.println();
        log.info("==================>结束新建订单"+"\t"+"xid_order:" +xid);



    }
}
