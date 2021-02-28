package com.itczy.org.sentinel;

import com.alibaba.csp.sentinel.slots.block.BlockException;

public class TestFallBack {

    /**
     * 必须是static
     */
    public static String fallback(String str, BlockException e){
        return "被限流或降级了 fallback";
    }
}
