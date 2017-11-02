package application.job;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import org.springframework.beans.factory.annotation.Autowired;
import zheng.log.core.common.MailManager;
import zheng.log.core.common.TransportClientManager;
import zheng.log.core.common.WeixinManager;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by alan.zheng on 2017/10/26.
 */
public class MonitorLogJob implements SimpleJob {
    @Autowired
    private TransportClientManager transportClientManager;
    @Autowired
    private WeixinManager weixinManager;
    @Autowired
    private MailManager mailManager;

    public void execute(ShardingContext shardingContext) {
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
        JSONArray jsonArray = transportClientManager.monitorLog(logname,"logs");
        mailManager.sendMonitorMail(jsonArray,"zheng.wenchao@yrw.com");
//        if (jsonArray!=null && jsonArray.size()>0){
//            for (int i=0;i<jsonArray.size();i++){
//                JSONObject jsonObject = (JSONObject)jsonArray.get(i);
//                int logCount = jsonObject.getIntValue("count");
//                if (logCount>5){
//                    try {
//                        weixinManager.monitorNotice(jsonObject.getIntValue("count"),jsonObject.getString("log"));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
    }
}
