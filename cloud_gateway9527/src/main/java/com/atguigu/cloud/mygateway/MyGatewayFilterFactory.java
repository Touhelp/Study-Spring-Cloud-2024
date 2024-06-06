package com.atguigu.cloud.mygateway;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.AbstractNameValueGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * 过滤掉没有携带key为statusKey的路径参数
 */
@Component
public class MyGatewayFilterFactory extends AbstractGatewayFilterFactory<MyGatewayFilterFactory.Config> {

    public static final String STATUS_KEY = "statusKey";

    public MyGatewayFilterFactory() {
        super(MyGatewayFilterFactory.Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                ServerHttpRequest request = exchange.getRequest();
                System.out.println("进入自定义的过滤器需要携带Key: " + config.getStatusKey());
                //获取用户的请求参数
                if (request.getQueryParams().containsKey(config.getStatusKey())){
                    //放行
                    return chain.filter(exchange);
                } else {
                    //拒绝请求
                    exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
                    return exchange.getResponse().setComplete();
                }

            }
        };
    }

    /**
     * 配置文件中属性名-设置紧凑型写法
     * @return
     */
    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList(STATUS_KEY);
    }

    /**
     * 用户类型，即请求携带的参数
     */
    @Validated
    public static class Config{

        @Getter@Setter@NotNull
        private String statusKey;
    }

}
