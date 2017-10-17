package zheng.log.common;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.alibaba.fastjson.JSONObject;
import com.sun.jmx.snmp.Timestamp;

import java.text.SimpleDateFormat;

/**
 * Created by alan.zheng on 2017/10/16.
 */
public class JsonLoggerAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {
    @Override
    public void start() {
        super.start();
        System.out.print("JsonLoggerAppender开始");
    }

    @Override
    public void stop() {
        super.stop();
        System.out.print("JsonLoggerAppender结束");
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
        System.out.print(json);
//        loggingEvent.getLoggerName();
    }
}
