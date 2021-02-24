package com.itczy.org.configuration;

import configurations.RibbonsConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.stereotype.Component;

@Component
//Ribbon配置，对user-center服务生效
@RibbonClient(name="user-center", configuration = RibbonsConfiguration.class)
//Ribbon全局配置，作用于所有的服务
//@RibbonClients(defaultConfiguration = RibbonsConfiguration.class)
public class RibbonConfiguration {
}
