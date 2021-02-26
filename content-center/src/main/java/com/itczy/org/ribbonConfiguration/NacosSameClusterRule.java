package com.itczy.org.ribbonConfiguration;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.core.Balancer;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 实现同集群下服务优先调用
 *
 * 提高容灾，比如在北京和上海都部署了机器，让北京的优先调用北京机房的服务，上海优先调用上海机房的服务
 */
@Slf4j
public class NacosSameClusterRule extends AbstractLoadBalancerRule {

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }

    @Override
    public Server choose(Object o) {
        try {
            //1.拿到配置文件中的集群 BJ/SH
            String clusterName = nacosDiscoveryProperties.getClusterName();
            //实现该方法，就可以自定义负载均衡的规则
            BaseLoadBalancer loadBalancer = (BaseLoadBalancer) this.getLoadBalancer();
            //想要请求的服务的名称
            String name = loadBalancer.getName();
            //拿到服务发现相关的API
            NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();

            //2.找到指定服务的所有实例 A
            //true表示只拿健康的实例
            List<Instance> instances = namingService.selectInstances(name, true);

            //3.过滤相同集群下的所有实例 B;
            //stream是JDK8的新特性，需要了解，常常和Lambda表单式一起使用
            List<Instance> sameInstances = instances.stream()
                    .filter(instance -> Objects.equals(instance.getClusterName(), clusterName))
                    .collect(Collectors.toList());
            //4.如果B为空，就使用A
            if(CollectionUtils.isEmpty(sameInstances)){
                sameInstances = instances;
                log.info("发生跨集群调用");
            }

            //5.基于权重算法返回一个实例
            Instance hostByRandomWeight = BalanceIns.getHostByRandomWeight(sameInstances);
            log.info("port=",hostByRandomWeight.getPort());

            return new NacosServer(hostByRandomWeight);
        } catch (NacosException e) {
            e.printStackTrace();
            log.error("发生异常了", e);
            return null;
        }
    }
}

//继承复写父类负载均衡核心算法，父类是protected类型，不能直接使用
class BalanceIns extends Balancer{
    public static Instance getHostByRandomWeight(List<Instance> hosts) {
        return Balancer.getHostByRandomWeight(hosts);
    }
}