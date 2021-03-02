package com.itczy.org.aspect;

import com.itczy.org.exception.SecurityException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

@Aspect
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CheckAuthorizationAspect {

    @Around("@annotation(com.itczy.org.aspect.CheckAuthorization)")
    public Object checkAuthorizationAspect(ProceedingJoinPoint point){
        try {
            //1.验证token是否合法

            //2.验证用户角色是否匹配
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
            HttpServletRequest request = servletRequestAttributes.getRequest();
            String role =(String) request.getAttribute("role");

            MethodSignature signature = (MethodSignature) point.getSignature();
            Method method = signature.getMethod();

            //此处通过反射拿注解的值，需要给注解加上 @Retention(RetentionPolicy.RUNTIME)
            CheckAuthorization annotation = method.getAnnotation(CheckAuthorization.class);

            if(!Objects.equals(role, annotation.value())){
                throw new SecurityException("用户无权访问");
            }

            return point.proceed();
        } catch (Throwable throwable) {
            throw new SecurityException("用户无权访问",throwable);
        }
    }
}
