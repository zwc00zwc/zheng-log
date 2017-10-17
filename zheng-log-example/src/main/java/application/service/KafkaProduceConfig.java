package application.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zheng.log.core.kafka.KafkaProducerManager;

/**
 * Created by alan.zheng on 2017/10/16.
 */
//@Configuration
public class KafkaProduceConfig {
    @Bean(initMethod = "init",name = "kafkaProducerManager" )
    public KafkaProducerManager kafkaProducerManager() {
        return new KafkaProducerManager("192.168.48.129:9092");
    }
}
