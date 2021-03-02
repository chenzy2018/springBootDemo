package com.itczy.org.aspect;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 为了反射可以拿到value属性的值
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckAuthorization {


    /**
     * SystemAdmin：表示系统管理员
     */
    String value();
}
