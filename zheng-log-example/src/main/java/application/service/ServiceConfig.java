package application.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zheng.log.core.common.TransportClientManager;

/**
 * Created by alan.zheng on 2017/10/12.
 */
@Configuration
public class ServiceConfig {

    @Bean(initMethod = "init",name = "transportClientManager" )
    public TransportClientManager dataflowJobScheduler() {
        return new TransportClientManager("elasticSearch-zheng","node-1","192.168.48.129:9300,192.168.48.131:9300,192.168.48.132:9300");
    }
}
