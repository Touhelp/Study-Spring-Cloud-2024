package com.atguigu.cloud.handler;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class MyRequestOriginParser implements RequestOriginParser
{
    /**
     * 从请求头里面获取值，作为请求的来源之一
     * 在运行时，当一个 HTTP 请求到达并需要进行限流控制时，Sentinel 会调用 parseOrigin 方法来获取请求的来源。
     * 根据解析到的来源信息，Sentinel 可以应用不同的限流规则。例如，可以为不同的 serverName 配置不同的 QPS（每秒查询数）限制。
     * @param request HTTP request
     * @return
     */
    @Override
    public String parseOrigin(HttpServletRequest request) {
        return request.getParameter("serverName");
    }
}
