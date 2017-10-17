package zheng.log.common;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.alibaba.fastjson.JSONObject;
import com.sun.jmx.snmp.Timestamp;
import zheng.log.core.kafka.KafkaProducerManager;

import java.text.SimpleDateFormat;

/**
 * Created by alan.zheng on 2017/10/16.
 */
public class JsonLoggerAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {
    private KafkaProducerManager kafkaProducerManager;
    @Override
    public void start() {
        System.out.print("JsonLoggerAppender开始");
        kafkaProducerManager = new KafkaProducerManager("192.168.48.129:9092,192.168.48.131:9092,192.168.48.132:9092");
        kafkaProducerManager.init();
        super.start();
    }

    @Override
    public void stop() {
        System.out.print("JsonLoggerAppender结束");
        kafkaProducerManager.closed();
        super.stop();
    }

    @Override
    protected void append(ILoggingEvent loggingEvent) {

        String loggerId = LoggerUtility.getLoggerId();
        LoggerModel loggerModel = new LoggerModel();
        loggerModel.setLoggerId(loggerId);
        loggerModel.setLoggerName(loggingEvent.getLoggerName());
        loggerModel.setLevel(loggingEvent.getLevel().toString());
        loggerModel.setThread(loggingEvent.getThreadName());
        loggerModel.setMessage(loggingEvent.getMessage());
        StringBuilder stringBuilder = new StringBuilder();
        if (loggingEvent.getThrowableProxy()!=null){
            stringBuilder.append(loggingEvent.getThrowableProxy().getClassName());
            StackTraceElementProxy[] step = loggingEvent.getThrowableProxy().getStackTraceElementProxyArray();
            if (step!=null && step.length>0){
                for (int i =0;i<step.length;i++){
                    stringBuilder.append(step[i].getSTEAsString());
                }
            }
        }
        loggerModel.setMessageDetail(stringBuilder.toString());
        Timestamp t = new Timestamp(loggingEvent.getTimeStamp());
        SimpleDateFormat sdfFrom = null;
        String sRet = null;
        try {
            sdfFrom = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sRet = sdfFrom.format(t.getDate()).toString();
        } catch (Exception ex) {
        } finally {
            sdfFrom = null;
        }
        loggerModel.setDate(sRet);
        String json = JSONObject.toJSONString(loggerModel);
//        System.out.print("当前日志id:" + loggerId );
//        System.out.print(loggingEvent.getMessage());
        try {
            kafkaProducerManager.send("yrw-log",loggerId,json);
        } catch (Exception e) {
            System.out.print("kafkaProducerManager异常");
        }
//        System.out.print(json);
//        loggingEvent.getLoggerName();
    }
}
