package zheng.log.core.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * Created by alan.zheng on 2017/10/17.
 */
public class KafkaProducerManager {
    private final String servers;
    private Producer<String, String> producer;

    public KafkaProducerManager(String _servers){
        servers = _servers;
    }

    public void init(){
        Properties props = new Properties();
        props.put("bootstrap.servers", servers);
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<String, String>(props);
    }

    public void send(String topic,String key,String data){
        producer.send(new ProducerRecord<String, String>(topic, key, data));
        producer.flush();
    }

    public void closed(){
        producer.close();
    }

    public static void main(String[] args){
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.48.129:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<String, String>(props);
        for (int i = 0; i < 10; i++)
            producer.send(new ProducerRecord<String, String>("first-topic", Integer.toString(i), Integer.toString(i)));

        producer.close();
    }
}
