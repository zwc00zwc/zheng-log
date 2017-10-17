package zheng.log.core.kafka;

import com.google.common.base.Optional;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Created by alan.zheng on 2017/10/17.
 */
public class SpringConsumerListener extends ConsumerListener {
    private final AbstractConsumer abstractConsumer;
//    private final KafkaConsumerManager kafkaConsumerManager;

    public SpringConsumerListener(String _className,AbstractConsumer abstractConsumer, ThreadPoolTaskExecutor threadPoolTaskExecutor,KafkaConsumerManager _kafkaConsumerManager){
        super(_className,threadPoolTaskExecutor,_kafkaConsumerManager);
        this.abstractConsumer=abstractConsumer;
    }

    @Override
    protected Optional<AbstractConsumer> createBaseConsumerInstance() {
        return Optional.fromNullable(abstractConsumer);
    }
}
