package com.itczy.org.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.itczy.org.domain.dto.TestDTO;
import com.itczy.org.domain.dto.UserDTO;
import com.itczy.org.sentinel.TestBlock;
import com.itczy.org.sentinel.TestFallBack;
import com.itczy.org.service.ContentService;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.http.*;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/content")
@Slf4j
public class ContentController {

    @Autowired
    private ContentService contentService;

    @GetMapping("/getContent")
    public UserDTO getContent(@RequestHeader("X-Token") String token){
        log.info("这是个log");
        return contentService.getContent(token);
    }

    /**
     * sentinel核心API不太优雅的使用方式，但是是偏底层的写法
     *
     * @param str
     * @return
     */
    @GetMapping("/test-sentinel-api")
    public String testSentinelApi(@RequestParam(value = "str") String str){
        String resourceName = "test-sentinel-api";

        //ContextUtil sentinel核心API，用于实现调用来源，标记调用
        //content-lk可填写在流控规则的针对来源
        ContextUtil.enter(resourceName,"content-lk");
        Entry entry = null;
        try {
            //SphO 底层还是调用SphU，只是返回值变成了boolean，可以结合if判断
            //SphO.entry(resourceName);
            //SphU sentinel核心API，用于定义一个资源，让资源收到监控，保护资源
            entry = SphU.entry(resourceName);
            //被保护的业务逻辑
            if(StringUtils.isBlank(str)) throw new IllegalArgumentException("str不能为空");
            return str;
        }
        //如果被保护的资源被限流或者降级了，就会抛BlockException
        catch (BlockException e) {
            e.printStackTrace();
            log.info("被限流或降级了", e);
            return "被限流或降级了";
        }catch (IllegalArgumentException e){
            //sentinel默认只会检测BlockException以及BlockException的子类的异常次数，其他异常不会统计
            //Tracer sentinel的核心API，让sentinel统计传入异常的发生次数、发生占比等信息，方便降级
            Tracer.trace(e);
            return "参数非法";
        }finally {
            if(entry != null){
                //退出entry
                entry.exit();
            }
            //退出ContextUtil
            ContextUtil.exit();
        }
    }

    /**
     * sentinel核心API优雅的使用方式，底层使用还是上面那种方式
     * blockHandler用于指定处理BlockException的函数
     * blockHandlerClass用于指定处理BlockException的类，提高代码重用率，简化当前类
     * fallback用于具体返回是限流了还是降级了
     * fallbackClass用于指定fallback处理类，提高代码重用率，简化当前类，1.6之后才支持
     */
    @GetMapping("/test-sentinel-resouce")
    @SentinelResource(value = "test-sentinel-api",
            blockHandler = "block",
            blockHandlerClass =  TestBlock.class,
            fallback = "fallback",
            fallbackClass = TestFallBack.class)
    public String testSentinelResouce(@RequestParam(value = "str") String str){

        //SentinelResource不支持来源
        //ContextUtil.enter(resourceName,"content-lk");

        //SentinelResource默认就会监控所有一样，因此不用单独调用Tracer
        //被保护的业务逻辑
        if(StringUtils.isBlank(str)) throw new IllegalArgumentException("str不能为空");
        return str;
    }

    /**
     * 必须和调用方有同样返回值，并包含调用方的入参
     * 处理降级或限流
     */
    public String block(String str, BlockException e){
        log.info("被限流或降级了 block", e);
        return "被限流或降级了 block";
    }

    /**
     * 必须和调用方有同样返回值，并包含调用方的入参
     * 1.5之前只用于降级
     * 1.6之后可以处理降级和限流，才支持BlockException
     */
    public String fallback(String str, BlockException e){
        return "被限流或降级了 fallback";
    }

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/testRestTemplate/{userId}")
    public TestDTO testRestTemplate(@PathVariable int userId){
        return restTemplate.getForObject("http://user-centent/getUser/{userId}",TestDTO.class, userId);
    }

    /**
     * 使用exchange方法支持Token传递
     * 会面对大量修改代码的问题
     * 对于所有restTemplate都共用的修改，使用ClientHttpRequestInterceptor方式做全局处理比较好
     */
    @GetMapping("/testTokenRelay/{userId}")
    public ResponseEntity<TestDTO> testTokenRelay(@PathVariable int userId, HttpServletRequest httpRequest){

        //可以在方法上注入HttpServletRequest的方式，也可以是用静态方式获取Token
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Token",httpRequest.getHeader("X-Token"));

        return
                restTemplate.exchange(
                    "http://user-centent/getUser/{userId}",
                    HttpMethod.GET,
                    new HttpEntity<>(httpHeaders),
                    TestDTO.class,
                    userId
                );
    }

    @Autowired(required = false)
    private Source source;

    @GetMapping("test-stream")
    public String testStream(){
        //直接发送消息，也可以指定超时时间的send
        source.output().send(
                MessageBuilder.withPayload("消息体").build()
        );
        return "seccuss";
    }

}
