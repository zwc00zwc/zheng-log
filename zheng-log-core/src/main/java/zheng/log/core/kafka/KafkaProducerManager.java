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
        //acks=0 producter不需要等待任何确认收到的信息。副本将立即加到socket buffer并认为已经发送，没有重试。数据丢失可能性高
        //acks=1 至少等待leader已经成功将数据写入本地log，不用等待所有follower是否成功写入，当leader宕机，可能发生数据丢失
        //acks=all 需要leader等待所有备份都成功写入日志，
        props.put("acks", "1");
        //重试机会，设置大于0
        props.put("retries", 0);
        //当多个记录被发送到同一个分区时，生产者会尝试将记录合并到更少的请求中
        props.put("batch.size", 16384);
        //高负载下的发送延时
        props.put("linger.ms", 1);
        //producter可以用来缓存数据的内存大小。如果数据产生数据大于想broker发送的速度，producter会阻塞或者抛出异常
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<String, String>(props);
    }

    public void send(String topic,String key,String data){
        producer.send(new ProducerRecord<String, String>(topic, key, data));
        //producer.flush();
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
