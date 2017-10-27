package application.config;

import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by alan.zheng on 2017/10/26.
 */
@Configuration
public class RegistryCenterConfig {
    @Bean(initMethod = "init",name = "zookeeperRegistryCenter")
    public ZookeeperRegistryCenter regCenter(@Value("${reg.serverList}") String serverList,@Value("${reg.namespace}") String namespace) {
        return new ZookeeperRegistryCenter(new ZookeeperConfiguration(serverList, namespace));
    }
}
