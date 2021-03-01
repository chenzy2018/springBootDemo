package com.itczy.org;

import com.itczy.org.config.TimeBetweenConfig;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * 自定义谓词工厂，实现时间区间
 *
 * 文件名必须以RoutePredicateFactory结尾，这是约定
 *
 * 需要加 @Component 注解
 */
@Component
public class TimeBetweenRoutePredicateFactory extends AbstractRoutePredicateFactory<TimeBetweenConfig> {

    public TimeBetweenRoutePredicateFactory() {
        super(TimeBetweenConfig.class);
    }

    /**
     * 控制路由的条件
     * @param config
     * @return
     */
    @Override
    public Predicate<ServerWebExchange> apply(TimeBetweenConfig config) {
        LocalTime start = config.getStart();
        LocalTime end = config.getEnd();

        //和下面的一个意思，lambda简写
        return exchange -> {
            LocalTime localTime = LocalTime.now();
            return localTime.isAfter(start) && localTime.isBefore(end);
        };
        /*return new Predicate<ServerWebExchange>() {
            @Override
            public boolean test(ServerWebExchange exchange) {
                LocalTime localTime = LocalTime.now();
                return localTime.isAfter(start) && localTime.isBefore(end);
            }
        };*/
    }

    /**
     * 指定顺序
     * @return
     */
    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("start","end");
    }
}
