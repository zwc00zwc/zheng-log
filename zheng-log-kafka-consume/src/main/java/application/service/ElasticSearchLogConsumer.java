package application.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import zheng.log.core.common.LoggerModel;
import zheng.log.core.common.TransportClientManager;
import zheng.log.core.kafka.AbstractConsumer;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by alan.zheng on 2018/4/26.
 */
public class ElasticSearchLogConsumer extends AbstractConsumer {
    @Autowired
    private TransportClientManager transportClientManager;
    @Override
    public void work(ConsumerRecord<String, String> record) {
        if (StringUtils.isEmpty(record.value())){
            return;
        }
        JSONObject loggerModel = JSON.parseObject(record.value());
        Date date = new Date();
        SimpleDateFormat sdfFrom = null;
        String sRet = null;
        try {
            sdfFrom = new SimpleDateFormat("yyyy-MM-dd");
            sRet = sdfFrom.format(date).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String logname = "java-kafka-"+sRet;
        transportClientManager.addIndexAndDocument(logname,"logs",loggerModel);
        System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
    }
}
