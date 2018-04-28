package application.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import zheng.log.core.common.TransportClientManager;
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
        return new KafkaConsumerManager("192.168.48.129:9092,192.168.48.131:9092,192.168.48.132:9092","yrw-log-group", Arrays.asList("yourong-log"));
    }

    @Bean(initMethod = "initSingle",name = "transportClientManager" )
    public TransportClientManager transportClientManager(@Value("${elasticSearch.servers}")String servers) {
        return new TransportClientManager(servers);
    }

    @Bean(name = "testLogConsumer")
    public TestLogConsumer testLogConsumer() {
        return new TestLogConsumer();
    }

    @Bean(name = "elasticSearchLogConsumer")
    public ElasticSearchLogConsumer elasticSearchLogConsumer() {
        return new ElasticSearchLogConsumer();
    }

    @Bean(initMethod = "init",name = "consumerListener")
    public ConsumerListener consumerListener(final ElasticSearchLogConsumer elasticSearchLogConsumer, ThreadPoolTaskExecutor threadPoolTaskExecutor, KafkaConsumerManager kafkaConsumerManager) {
        return new SpringConsumerListener(elasticSearchLogConsumer.getClass().getCanonicalName(),elasticSearchLogConsumer,threadPoolTaskExecutor,kafkaConsumerManager);
    }
}
