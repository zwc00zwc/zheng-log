package zheng.log.core.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

/**
 * Created by alan.zheng on 2017/10/17.
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
        //建立与kafka集群的初始连接
        props.put("bootstrap.servers", servers);
        //此消费者所属的消费者组
        props.put("group.id", groupId);
        //消费者的偏移将在后台定期提交
        props.put("enable.auto.commit", "true");
        //消费者偏移量自动提交给kafka的频率
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(topics);
//        while (true) {
//            ConsumerRecords<String, String> records = consumer.poll(100);
//            for (ConsumerRecord<String, String> record : records)
//                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
//        }
    }

    public void receive(AbstractConsumer abstractConsumer){
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records)
                abstractConsumer.work(record);
        }
    }

    public static void main(String[] args){
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.48.129:9092");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Arrays.asList("foo", "bar"));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records)
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
        }
    }
}
