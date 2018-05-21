package zheng.log.core.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.alibaba.fastjson.JSONObject;
import com.sun.jmx.snmp.Timestamp;
import zheng.log.core.common.LoggerModel;
import zheng.log.core.common.LoggerUtility;
import zheng.log.core.kafka.KafkaProducerManager;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;

/**
 * Created by alan.zheng on 2017/10/16.
 */
public class KafkaLoggerAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {
    private String servers;

    private String topic;

    private KafkaProducerManager kafkaProducerManager;
    @Override
    public void start() {
        System.out.print("JsonLoggerAppender开始");
        kafkaProducerManager = new KafkaProducerManager(servers);
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

        String sessionId = LoggerUtility.getSessionId();
        LoggerModel loggerModel = new LoggerModel();
        loggerModel.setLocalIp(getLocalIP());
        loggerModel.setRequestIp(LoggerUtility.getRequestIp());
        loggerModel.setSessionId(sessionId);
        loggerModel.setLoggerName(loggingEvent.getLoggerName());
        loggerModel.setLevel(loggingEvent.getLevel().toString());
        loggerModel.setThread(loggingEvent.getThreadName());
        loggerModel.setMessage(loggingEvent.getMessage());
        StringBuilder stringBuilder = new StringBuilder();
        if (loggingEvent.getThrowableProxy()!=null){
            stringBuilder.append(loggingEvent.getThrowableProxy().getClassName() + "\n");
            StackTraceElementProxy[] step = loggingEvent.getThrowableProxy().getStackTraceElementProxyArray();
            if (step!=null && step.length>0){
                for (int i =0;i<step.length;i++){
                    if (i==step.length-1){
                        stringBuilder.append(step[i].getSTEAsString());
                    }else {
                        stringBuilder.append(step[i].getSTEAsString() + "\n");
                    }
                }
            }
        }
        loggerModel.setMessageDetail(stringBuilder.toString());
        Timestamp t = new Timestamp(loggingEvent.getTimeStamp());
        SimpleDateFormat sdfFrom = null;
        String sRet = null;
        try {
            sdfFrom = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZ");
            sRet = sdfFrom.format(t.getDate());
        } catch (Exception ex) {
        } finally {
            sdfFrom = null;
        }
        loggerModel.setDate(sRet);
        String json = JSONObject.toJSONString(loggerModel);
//        System.out.print("当前日志id:" + loggerId );
//        System.out.print(loggingEvent.getMessage());
        try {
            kafkaProducerManager.send(topic,sessionId,json);
        } catch (Exception e) {
            System.out.print("kafkaProducerManager异常");
        }
//        System.out.print(json);
//        loggingEvent.getLoggerName();
    }

    private String getLocalIP(){
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        byte[] ipAddr = addr.getAddress();
        String ipAddrStr = "";
        for (int i = 0; i < ipAddr.length; i++) {
            if (i > 0) {
                ipAddrStr += ".";
            }
            ipAddrStr += ipAddr[i] & 0xFF;
        }
        //System.out.println(ipAddrStr);
        return ipAddrStr;
    }

    public String getServers() {
        return servers;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
