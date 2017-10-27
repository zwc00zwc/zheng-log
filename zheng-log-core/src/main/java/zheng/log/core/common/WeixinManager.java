package zheng.log.core.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * Created by alan.zheng on 2017/10/27.
 */
@Component
public class WeixinManager {
    private static Logger logger = LoggerFactory.getLogger(WeixinManager.class);
    private static final String tokenUrl = "https://api.weixin.qq.com/cgi-bin/token";

    private static final String grantTypeForToken = "client_credential";

    private static final String redisWenxin = "redis_wenxin";

    @Value("${weixin.url}")
    private String url;

    @Value("${weixin.appid}")
    private String appid;

    @Value("${weixin.appsecret}")
    private String appsecret;

    @Autowired
    private HttpRequestClient httpRequestClient;

    @Autowired
    private RedisManager redisManager;

    private Map<String, Object> getAccessToken(String appid, String secret){
        String url = tokenUrl+"?grant_type=client_credential&"+"appid="+appid+"&secret="+secret;
        String retCode = httpRequestClient.doGet(url);
        if (StringUtils.isBlank(retCode)) {
            logger.error("微信JsApi获取accessToken返回失败: retCode={}" + retCode);
            return null;
        }
        Map<String, Object> tokenMap = JSON.parseObject(retCode, java.util.HashMap.class);
        String accessToken = tokenMap.get("access_token").toString();
        String expiresIn = tokenMap.get("expires_in").toString();
        logger.info("获取微信jsAccessToken返回： accessToken={},expiresIn={}", accessToken, expiresIn);
        if (StringUtils.isBlank(accessToken) || StringUtils.isBlank(expiresIn)) {
            logger.error("微信JsApi获取accessToken返回失败:" + tokenMap);
            return null;
        }
        logger.info("微信JsApi获取accessToken返回:" + tokenMap);
        return tokenMap;
    }

    public void monitorNotice(Integer count, String logName) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("touser","oYzLx0oYFJyaV3qGprKHm6DSRHBA");
        jsonObject.put("template_id","7T32zTnyTap7Aea8HPdja4P3gtNr-HFd6v-aHNS4yUc");

        JSONObject dataJsonObject = new JSONObject();
        JSONObject valueJsonObject = null;
        valueJsonObject = new JSONObject();
        valueJsonObject.put("value","日志监控通知");
        valueJsonObject.put("color","#173177");
        dataJsonObject.put("first",valueJsonObject);
        valueJsonObject = new JSONObject();
        valueJsonObject.put("value", "5秒");
        valueJsonObject.put("color","#173177");
        dataJsonObject.put("keyword1",valueJsonObject);
        valueJsonObject = new JSONObject();
        valueJsonObject.put("value",count);
        valueJsonObject.put("color","#173177");
        dataJsonObject.put("keyword2",valueJsonObject);
        valueJsonObject = new JSONObject();
        valueJsonObject.put("value",logName);
        valueJsonObject.put("color","#173177");
        dataJsonObject.put("remark",valueJsonObject);
        jsonObject.put("data",dataJsonObject);
        //taskExecutor.execute(new SendWeixinMsgThread(jsonObject));
        send(jsonObject);
    }

    private void send(JSONObject jsonObject){
        String accessToken = getAccessToken();
        String postUrl="https://api.weixin.qq.com/cgi-bin/message/template/send";
        String retCode = httpRequestClient.doJsonPost(postUrl+"?access_token="+accessToken+"", jsonObject.toString());
        if (StringUtils.isBlank(retCode)) {
            logger.error("微信发送模板消息无返回");
        }
        Map<String, Object> ticketMap = JSON.parseObject(retCode, java.util.HashMap.class);
        String errcode = ticketMap.get("errcode").toString();
        String errmsg = ticketMap.get("errmsg").toString();
        if (!"0".equals(errcode)){
            logger.error("微信发送模板消息异常" + errmsg + "");
        }
    }

    private String getAccessToken(){
        // 从redis获取缓存
        String token = redisManager.getString(redisWenxin);
        if (StringUtils.isNotBlank(token)) {
            return token;
        }
        // 重新获取token
        return reflashToken();
    }

    private String reflashToken(){
        String getTokenurl = tokenUrl+"?grant_type="+grantTypeForToken +"&appid="+appid+"&secret="+appsecret;
        String retCode = httpRequestClient.doGet(getTokenurl);
        if (StringUtils.isBlank(retCode)) {
            logger.error("微信JsApi获取accessToken返回失败: retCode={}" + retCode);
            return null;
        }
        Map<String, Object> tokenMap = JSON.parseObject(retCode, java.util.HashMap.class);
        String accessToken = tokenMap.get("access_token").toString();
        String expiresIn = tokenMap.get("expires_in").toString();
        if (StringUtils.isBlank(accessToken) || StringUtils.isBlank(expiresIn)) {
            logger.error("微信JsApi获取accessToken返回失败:" + tokenMap);
            return null;
        }
        redisManager.putString(redisWenxin,accessToken);
        return accessToken;
    }
}
