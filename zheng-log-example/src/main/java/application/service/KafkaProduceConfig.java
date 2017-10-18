package application.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zheng.log.core.kafka.KafkaProducerManager;

/**
 * Created by alan.zheng on 2017/10/16.
 */
@Configuration
public class KafkaProduceConfig {

    @Bean(initMethod = "init",name = "kafkaProducerManager" )
    public KafkaProducerManager kafkaProducerManager(@Value("${kafka.servers}") final String servers) {
        return new KafkaProducerManager(servers);
    }
}
