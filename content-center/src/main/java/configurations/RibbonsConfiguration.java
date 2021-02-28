package configurations;

import com.itczy.org.ribbonConfiguration.TestNacosSameClusterRule;
import com.netflix.loadbalancer.IRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 该文件必须加@Configuration，必须放在启动类所在文件目录(即启动类可以扫描的文件目录)之外
 *
 * 存在 父子上下文 的问题，会导致很多无法预料的结果
 */
@Configuration
public class RibbonsConfiguration {

    @Bean
    public IRule ribbonRule(){
        //返回负载均衡的规则
        //return new TestNacosWeightRule();

        return new TestNacosSameClusterRule();
    }

}
