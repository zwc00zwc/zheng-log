package application.job;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import org.springframework.beans.factory.annotation.Autowired;
import zheng.log.core.common.TransportClientManager;
import zheng.log.core.common.WeixinManager;

/**
 * Created by alan.zheng on 2017/10/26.
 */
public class MonitorLogJob implements SimpleJob {
    @Autowired
    private TransportClientManager transportClientManager;
    @Autowired
    private WeixinManager weixinManager;

    public void execute(ShardingContext shardingContext) {
        JSONArray jsonArray = transportClientManager.monitorLog("java-kafka-2017.10.27","logs");
        if (jsonArray!=null && jsonArray.size()>0){
            for (int i=0;i<jsonArray.size();i++){
                JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                int logCount = jsonObject.getIntValue("count");
                if (logCount>5){
                    try {
                        weixinManager.monitorNotice(jsonObject.getIntValue("count"),jsonObject.getString("log"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
