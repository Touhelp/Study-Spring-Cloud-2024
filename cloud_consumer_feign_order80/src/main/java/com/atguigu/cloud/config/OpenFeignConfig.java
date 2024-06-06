package com.atguigu.cloud.config;

import feign.Logger;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.concurrent.TimeUnit.SECONDS;

@Configuration
public class OpenFeignConfig {

    @Bean
    public Retryer retryer(){
        //return new Retryer.Default(100, SECONDS.toMillis(1), 3);
        return Retryer.NEVER_RETRY;
    }

    /**
     * 配置日志Bean
     * @return
     */
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
