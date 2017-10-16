package zheng.kafka.core;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

/**
 * Created by alan.zheng on 2017/10/16.
 */
public class KafkaConsumerManager {
    private final String servers;
    private final String groupId;
    private final Collection<String> topics;

    private KafkaConsumer<String, String> consumer;

    public KafkaConsumerManager(String _servers,String _groupId,Collection<String> _topics){
        servers = _servers;
        groupId = _groupId;
        topics = _topics;
    }

    public void init(){
        Properties props = new Properties();
        //此处配置的是kafka的端口
        props.put("bootstrap.servers", servers);
        props.put("group.id",groupId);

        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        //配置value的序列化类
//        props.put("serializer.class", "kafka.serializer.StringEncoder");
//        //配置key的序列化类
//        props.put("key.serializer.class", "kafka.serializer.StringEncoder");
        consumer = new KafkaConsumer<String, String>(props);

        consumer.subscribe(topics);
        boolean flag = true;
        while (flag) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records)
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
        }
    }
}
