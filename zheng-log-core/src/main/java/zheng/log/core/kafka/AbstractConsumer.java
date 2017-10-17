package zheng.log.core.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * Created by alan.zheng on 2017/10/17.
 */
public interface AbstractConsumer {
    void work(String msg);
    void work(ConsumerRecord<String, String> record);
}
