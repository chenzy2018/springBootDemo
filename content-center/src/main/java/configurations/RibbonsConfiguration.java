package configurations;

import com.itczy.org.ribbonConfiguration.NacosSameClusterRule;
import com.netflix.loadbalancer.IRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 该文件必须放在启动类坐在文件目录之外
 *
 * 存在 上下文 的问题，会导致很多无法预料的结果
 */
@Configuration
public class RibbonsConfiguration {

    @Bean
    public IRule ribbonRule(){
        //返回负载均衡的规则
        //return new NacosWeightRule();

        return new NacosSameClusterRule();
    }

}
