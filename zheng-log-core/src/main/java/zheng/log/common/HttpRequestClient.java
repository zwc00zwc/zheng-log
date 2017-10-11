package zheng.log.common;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * Created by alan.zheng on 2017/10/11.
 */
public class HttpRequestClient {
    protected Logger logger = LoggerFactory.getLogger(HttpRequestClient.class);

    @Autowired
    private CloseableHttpClient httpClient;

    @Autowired
    private RequestConfig requestConfig;

    public String doJsonPost(String url, String json){

        // 创建http POST请求
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(this.requestConfig);
        HttpEntity entity = null;

        if (json != null) {
            // 构造一个form表单式的实体
            StringEntity stringEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
            // 将请求实体设置到httpPost对象中
            httpPost.setEntity(stringEntity);
        }

        CloseableHttpResponse response = null;

        try {
            // 执行请求
            try {
                response = this.httpClient.execute(httpPost);
            } catch (IOException e) {
                logger.error(e.getMessage(),e);
            }

            try {
                entity = response.getEntity();
                String  responseBody =  EntityUtils.toString(entity, "UTF-8");
                return responseBody;
            } catch (IOException e) {
                logger.error(e.getMessage(),e);
            }

        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(),e);
                }
            }
        }
        return null;
    }
}
