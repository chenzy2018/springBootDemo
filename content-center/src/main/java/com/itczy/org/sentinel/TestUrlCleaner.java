package com.itczy.org.sentinel;

import com.alibaba.csp.sentinel.adapter.servlet.callback.UrlCleaner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Slf4j
public class TestUrlCleaner implements UrlCleaner {

    @Override
    public String clean(String originUrl) {
        //让 /share/1 与 /share/2 的返回值相同
        //返回 /share/{number}

        String[] split = originUrl.split("/");

        return Arrays.stream(split)
                .map(string -> {
                    if(NumberUtils.isNumber(string)){
                        return "{number}";
                    }
                    return string;
                })
                .reduce((a,b) -> a + "/" + b)
                .orElse("");
    }
}
