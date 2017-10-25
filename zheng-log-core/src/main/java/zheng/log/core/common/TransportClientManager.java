package zheng.log.core.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.get.GetField;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by alan.zheng on 2017/10/12.
 */
public class TransportClientManager {
    private Logger logger = LoggerFactory.getLogger(TransportClientManager.class);

    private final String clusterName;
    private final String nodeName;
    private final String servers;

    private TransportClient client;

    public TransportClientManager(String _clusterName, String _nodeName, String _servers){
        clusterName = _clusterName;
        nodeName = _nodeName;
        servers = _servers;
    }

    public void init(){
        //x-pack
        Settings settings = Settings.builder()
                .put("cluster.name",clusterName)
                .put("xpack.security.transport.ssl.enabled",false)
                .put("xpack.security.user","elastic:changeme")
                .put("client.transport.sniff",true).build();

//        Settings settings = Settings.builder()
//                .put("cluster.name",clusterName).build();
//            client = new PreBuiltTransportClient(settings).addTransportAddresses(new InetSocketTransportAddress(InetAddress.getByName(host),port));

        try {
            //x-pack
            client = new PreBuiltXPackTransportClient(settings);
            String[] hosts = servers.split(",");
            if (hosts!=null&&hosts.length>0){
                for (int i=0; i<hosts.length; i++){
                    String h = hosts[i];
                    if (StringUtils.isNotEmpty(h)){
                        String[] hp = h.trim().split(":");
                        if (hp!=null&&hp.length==2){
                            client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(hp[0]), Integer.parseInt(hp[1])));
                        }
                    }
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

    public void shutDown(){
        if (client!=null){
            client.close();
        }
    }

    /**
     * 添加索引
     * @param article
     * @throws Exception
     */
    public void CreateIndexAndMapping(String article ,String content) throws Exception{

        CreateIndexRequestBuilder cib=client.admin().indices().prepareCreate(article);
        XContentBuilder mapping = XContentFactory.jsonBuilder()
                .startObject()
                .startObject("properties") //设置之定义字段
                .startObject("author")
                .field("type","string") //设置数据类型
                .endObject()
                .startObject("title")
                .field("type","string")
                .endObject()
                .startObject("content")
                .field("type","string")
                .endObject()
                .startObject("price")
                .field("type","string")
                .endObject()
                .startObject("view")
                .field("type","string")
                .endObject()
                .startObject("tag")
                .field("type","string")
                .endObject()
                .startObject("date")
                .field("type","date")  //设置Date类型
                .field("format","yyyy-MM-dd HH:mm:ss") //设置Date的格式
                .endObject()
                .endObject()
                .endObject();
        cib.addMapping(content, mapping);

        CreateIndexResponse res=cib.execute().actionGet();

        System.out.println("----------添加映射成功----------");
    }

    public JSONObject getIndexAndDocument(String index, String type, String id){
        GetResponse response = client.prepareGet(index,type,id).get();
        Map<String,Object> map = response.getSource();

        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(map));
        return jsonObject;
//        Map<String,GetField> map = response.getFields();
//        Iterator iterator = map.entrySet().iterator();
//        while (iterator.hasNext()){
//            Map.Entry entry = (Map.Entry)iterator.next();
//            jsonObject.put(entry.getKey().toString(),entry.getValue());
//        }
    }

    /**
     * 创建索引(没有id)
     * @param index
     * @param type
     * @param jsonObject
     */
    public void addIndexAndDocument(String index, String type, JSONObject jsonObject){
        try {
            XContentBuilder xContentBuilder = XContentFactory.jsonBuilder().startObject();
            bindField(xContentBuilder,jsonObject);
            xContentBuilder.endObject();
            IndexResponse response = client.prepareIndex(index,type).setSource(xContentBuilder).get();
            System.out.println("添加索引成功,版本号："+response.getVersion());
        } catch (IOException e) {
            logger.error("addIndexAndDocument 异常",e);
        }
    }

    /**
     * 创建索引
     * @param index
     * @param type
     * @param id
     * @param jsonObject
     */
    public void addIndexAndDocument(String index, String type, String id, JSONObject jsonObject){
        try {
            XContentBuilder xContentBuilder = XContentFactory.jsonBuilder().startObject();
            bindField(xContentBuilder,jsonObject);
            xContentBuilder.endObject();
            IndexResponse response = client.prepareIndex(index, type ,id).setSource(xContentBuilder).get();
            System.out.println("添加索引成功,版本号："+response.getVersion());
        } catch (IOException e) {
            logger.error("addIndexAndDocument 异常",e);
        }
    }

    /**
     * 删除index
     * @param index
     * @param type
     * @param id
     */
    public boolean deleteIndex(String index, String type, String id){
        DeleteResponse deleteResponse = client.prepareDelete(index,type,id).get();
        DocWriteResponse.Result result = deleteResponse.getResult();
        if (DocWriteResponse.Result.DELETED.equals(result)){
            return true;
        }
        return false;
    }

    private void bindField(XContentBuilder xContentBuilder, JSONObject jsonObject){
        if (jsonObject!=null){
            Set<String> keys = jsonObject.keySet();
            Iterator<String> iterator = keys.iterator();
            while (iterator.hasNext()){
                String key = iterator.next();
                try {
                    xContentBuilder.field(key,jsonObject.get(key));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
