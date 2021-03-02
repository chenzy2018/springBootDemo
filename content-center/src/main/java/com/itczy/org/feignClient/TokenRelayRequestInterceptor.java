package com.itczy.org.feignClient;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * token传递拦截器
 *
 * RequestInterceptor非常适合用于做fiegn远程接口公共的需求
 * 使用这个方式，后续需求变动，只需要修改这里，feignClient和调用feignClient的地方均不需要修改
 */
public class TokenRelayRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        //1.获取到tokne
        //静态获取request的方式，重要
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();

        String token = request.getHeader("x-Token");//假设是x-Token

        //2.将tokne传递
        if(StringUtils.isNotBlank(token)){
            template.header("X-Token", token);
        }
    }
}
