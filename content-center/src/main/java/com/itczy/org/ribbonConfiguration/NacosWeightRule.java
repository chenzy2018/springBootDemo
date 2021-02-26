package com.itczy.org.ribbonConfiguration;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 根据nacos设置的权重进行负载均衡
 *
 * 可以通过实现IRule接口或者继承AbstractLoadBalancerRule类
 */

@Slf4j
public class NacosWeightRule extends AbstractLoadBalancerRule {

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {
        //读取配置文件，并初始化，基本不会用，留空就行，ribbon中大部分实现也都是留空的
    }

    @Override
    public Server choose(Object o) {
        try {
            log.info("进入自定义负载均衡规则");
            //实现该方法，就可以自定义负载均衡的规则
            BaseLoadBalancer loadBalancer = (BaseLoadBalancer) this.getLoadBalancer();
            //想要请求的服务的名称
            String name = loadBalancer.getName();

            //实现负载均衡算法
            //拿到服务发现相关的API
            NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();

            //nacos client自动通过基于权重的负载均衡算法，返回一个实例
            Instance instance = namingService.selectOneHealthyInstance(name);
            log.info("prot=",instance.getPort());

            return new NacosServer(instance);
        } catch (NacosException e) {
            e.printStackTrace();
            return null;
        }
    }
}
