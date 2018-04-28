package zheng.log.core.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * Created by alan.zheng on 2017/10/17.
 */
public abstract class AbstractConsumer {
    public void work(String msg){

    }
    public void work(ConsumerRecord<String, String> record){

    }
}
