package com.itczy.org.sentinel;

import com.alibaba.csp.sentinel.adapter.servlet.callback.RequestOriginParser;
import com.alibaba.csp.sentinel.util.StringUtil;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 必须加@Component才能生效
 *
 * 实现区分来源
 * 下面的实现是放在链接上传递的，实际中应该放在header中传递
 */
@Component
public class TestRequestOriginParser implements RequestOriginParser {
    @Override
    public String parseOrigin(HttpServletRequest httpServletRequest) {
        //从请求参数中获取名为 origin 的参数并返回
        //如果获取不到origin参数，那么就抛异常

        String origin = httpServletRequest.getParameter("origin");
        if(StringUtil.isBlank(origin)){
            throw new IllegalArgumentException("origin 为空");
        }
        return origin;
    }
}
