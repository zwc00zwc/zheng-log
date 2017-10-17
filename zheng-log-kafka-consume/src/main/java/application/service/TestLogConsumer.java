package application.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import zheng.log.core.kafka.AbstractConsumer;

/**
 * Created by alan.zheng on 2017/10/17.
 */
public class TestLogConsumer implements AbstractConsumer {
    public void work(String msg) {

    }

    public void work(ConsumerRecord<String, String> record) {
        System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
    }
}
