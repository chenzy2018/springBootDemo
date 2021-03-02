package com.itczy.org.aspect;

import com.itczy.org.exception.SecurityException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class CheckLoginAspect {

    /**
     * 只要加了@CheckLogin的方法，都会走到这里
     *
     * @param point
     * @return
     */
    @Around("@annotation(com.itczy.org.aspect.CheckLogin)")
    public Object CheckLogin(ProceedingJoinPoint point){
        //1.从header里面获取token
        //静态获取request的方式，重要
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();

        String token = request.getHeader("x-Token");//假设是x-Token
        //2.校验token是否合法，如果合法放行，如果不合法则报错
        if(token == null){
            throw new SecurityException("Token不合法");
        }

        return "Token验证通过";
    }
}
