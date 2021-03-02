package com.itczy.org.feignClient;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * RestTemplate的拦截器，用于做RestTemplate的公共逻辑，比如增加Token
 */
public class TestRestTemplateTokenRelayInterceptor implements ClientHttpRequestInterceptor{
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        //1.获取到tokne
        //静态获取request的方式，重要
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest httpRequest = servletRequestAttributes.getRequest();

        String token = httpRequest.getHeader("x-Token");//假设是x-Token

        //2.将tokne传递
        if(StringUtils.isNotBlank(token)){
            request.getHeaders().add("X-Token", token);
        }

        //保证请求继续执行
        return execution.execute(request, body);
    }
}
