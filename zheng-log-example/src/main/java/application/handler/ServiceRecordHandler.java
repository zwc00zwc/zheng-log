package application.handler;

import application.annotation.ServiceRecord;
import com.alibaba.fastjson.JSONObject;
import com.sun.jmx.snmp.Timestamp;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import zheng.log.core.common.LoggerModel;
import zheng.log.core.common.LoggerUtility;
import zheng.log.core.kafka.KafkaProducerManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by alan.zheng on 2017/10/18.
 */
@Aspect
@Component
public class ServiceRecordHandler {

    @Value("${kafka.topic}")
    private String topic;

    @Autowired
    private KafkaProducerManager kafkaProducerManager;

    @Pointcut("@annotation(application.annotation.ServiceRecord)")
    public void methodPointcut(){

    }

    @Before("@annotation(serviceRecord)")
    public void before(ServiceRecord serviceRecord){
        String sessionId = LoggerUtility.getSessionId();
        if (StringUtils.isEmpty(sessionId)){
            HttpSession session = ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes()).getRequest().getSession();
            try {
                LoggerUtility.changeSessionId(session.getId());
            } catch (Exception e) {
                System.out.print("生成sessionId异常");
            }
        }
        LoggerModel loggerModel = new LoggerModel();
        loggerModel.setLocalIp(getLocalIP());
        loggerModel.setSessionId(sessionId);
        loggerModel.setLoggerName("Service Log");
        loggerModel.setThread(Thread.currentThread().getName());
        loggerModel.setMessage(serviceRecord.service());
        SimpleDateFormat sdfFrom = null;
        String sRet = null;
        try {
            sdfFrom = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sRet = sdfFrom.format(new Date()).toString();
        } catch (Exception ex) {
        }
        loggerModel.setDate(sRet);
        String json = JSONObject.toJSONString(loggerModel);
        kafkaProducerManager.send(topic,sessionId,json);
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
}
