package com.atguigu.cloud.mygateway;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.cloud.gateway.handler.predicate.AfterRoutePredicateFactory;
import org.springframework.cloud.gateway.handler.predicate.GatewayPredicate;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ServerWebExchange;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

@Component
public class VIPRoutePredicateFactory extends AbstractRoutePredicateFactory<VIPRoutePredicateFactory.Config> {

    /**
     * USER_TYPE key. 配置文件中属性的名字
     */
    public static final String USER_TYPE_KEY = "userType";

    public VIPRoutePredicateFactory() {
        super(Config.class);
    }

    /**
     * 在配置文件中支持紧凑型写法
     * @return
     */
    @Override
    public List<String> shortcutFieldOrder() {
        return Collections.singletonList(USER_TYPE_KEY);
    }

    /**
     * 比较方法
     * @param config
     * @return
     */
    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        return new GatewayPredicate() {
            @Override
            public boolean test(ServerWebExchange serverWebExchange) {
                String userType = serverWebExchange.getRequest().getQueryParams().getFirst("userType");
                //判空处理
                if (userType == null){
                    return false;
                }

                if (userType.equals(config.getUserType())){
                    return true;
                }
                return false;
            }
        };
    }

    @Validated //开启Bean的校验，配和@NotNull使用
    public static class Config {
        @Getter
        @Setter
        @NotNull
        private String userType;

    }

}
