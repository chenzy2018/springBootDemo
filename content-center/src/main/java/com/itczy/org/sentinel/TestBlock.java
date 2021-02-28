package com.itczy.org.sentinel;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestBlock {

    public static String block(String str, BlockException e){
        log.info("被限流或降级了 block", e);
        return "被限流或降级了 block";
    }
}
