package application.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zheng.log.core.common.TransportClientManager;

/**
 * Created by alan.zheng on 2017/10/12.
 */
@Configuration
public class ServiceConfig {

    @Bean(initMethod = "init",name = "transportClientManager" )
    public TransportClientManager dataflowJobScheduler(@Value("${elasticSearch.clusterName}")String clusterName, @Value("${elasticSearch.nodeName}")String nodeName,
                                                       @Value("${elasticSearch.servers}")String servers) {
        return new TransportClientManager(clusterName,nodeName,servers);
    }
}
