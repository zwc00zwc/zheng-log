package application.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import zheng.log.core.kafka.ConsumerListener;
import zheng.log.core.kafka.KafkaConsumerManager;
import zheng.log.core.kafka.SpringConsumerListener;

import java.util.Arrays;

/**
 * Created by alan.zheng on 2017/10/17.
 */
@Configuration
public class KafkaConsumerListenerConfig {
    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(){
        ThreadPoolTaskExecutor threadPoolExecutor=new ThreadPoolTaskExecutor();
        threadPoolExecutor.setCorePoolSize(5);
        threadPoolExecutor.setMaxPoolSize(10);
        threadPoolExecutor.setQueueCapacity(25);
        return new ThreadPoolTaskExecutor();
    }

    @Bean(initMethod = "init",name = "kafkaConsumerManager" )
    public KafkaConsumerManager kafkaConsumerManager() {
        return new KafkaConsumerManager("192.168.48.129:9092,192.168.48.131:9092,192.168.48.132:9092","yrw-log-group", Arrays.asList("yrw-log"));
    }

    @Bean(name = "testLogConsumer")
    public TestLogConsumer testLogConsumer() {
        return new TestLogConsumer();
    }

    @Bean(initMethod = "init",name = "consumerListener")
    public ConsumerListener consumerListener(final TestLogConsumer testLogConsumer, ThreadPoolTaskExecutor threadPoolTaskExecutor, KafkaConsumerManager kafkaConsumerManager) {
        return new SpringConsumerListener(testLogConsumer.getClass().getCanonicalName(),testLogConsumer,threadPoolTaskExecutor,kafkaConsumerManager);
    }
}
