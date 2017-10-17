package application.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zheng.kafka.core.KafkaConsumerManager;

import java.util.Arrays;

/**
 * Created by alan.zheng on 2017/10/16.
 */
@Configuration
public class KafkaConsumeConfig {
    @Bean(initMethod = "init",name = "kafkaConsumerManager" )
    public KafkaConsumerManager dataflowJobScheduler() {
        return new KafkaConsumerManager("192.168.48.129:9092","first-group", Arrays.asList("first-topic"));
    }
}
