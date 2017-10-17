package zheng.log.core.kafka;

import com.google.common.base.Optional;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Created by alan.zheng on 2017/10/17.
 */
public class ConsumerListener {
    private final String consumerClass;
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private final KafkaConsumerManager kafkaConsumerManager;

    public ConsumerListener(final String _consumerClass, final ThreadPoolTaskExecutor _threadPoolTaskExecutor, KafkaConsumerManager _kafkaConsumerManager){
        consumerClass = _consumerClass;
        threadPoolTaskExecutor = _threadPoolTaskExecutor;
        kafkaConsumerManager = _kafkaConsumerManager;
    }

    /**
     * 初始化消息监听
     */
    public void init(){
        Optional<AbstractConsumer> abstractConsumerOptional= createBaseConsumerInstance();
        if (abstractConsumerOptional.isPresent()){
            execute(kafkaConsumerManager,abstractConsumerOptional.get());
        }else {
            try {
                AbstractConsumer abstractConsumer=(AbstractConsumer) Class.forName(consumerClass).newInstance();
                execute(kafkaConsumerManager,abstractConsumer);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    protected Optional<AbstractConsumer> createBaseConsumerInstance() {
        return Optional.absent();
    }

    private void execute(final KafkaConsumerManager kafkaConsumer, final AbstractConsumer abstractConsumer){
        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                kafkaConsumer.receive(abstractConsumer);
            }
        });
    }
}
