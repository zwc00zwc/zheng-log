package zheng.log.core.common;

import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by alan.zheng on 2017/10/12.
 */
public class TransportClientManager {
    private Logger logger = LoggerFactory.getLogger(TransportClientManager.class);

    private final String clusterName;
    private final String nodeName;
    private final String host;
    private final Integer port;

    private TransportClient client;

    public TransportClientManager(String _clusterName, String _nodeName, String _host, Integer _port){
        clusterName = _clusterName;
        nodeName = _nodeName;
        host = _host;
        port = _port;
    }

    public void init(){
        Settings settings = Settings.builder().put(clusterName,nodeName).put(clusterName,true).build();
        try {
            client = new PreBuiltTransportClient(settings).addTransportAddresses(new InetSocketTransportAddress(InetAddress.getByName(host),port));
        } catch (UnknownHostException e) {
            e.printStackTrace();
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

    public void addIndexAndDocument(String article, String content, JSONObject jsonObject){

//        Date time = new Date();

//        IndexResponse response = client.prepareIndex(article, content)
//                .setSource(XContentFactory.jsonBuilder().startObject()
//                        .field("id","447")
//                        .field("author","fendo")
//                        .field("title","192.138.1.2")
//                        .field("content","这是JAVA有关的书籍")
//                        .field("price","20")
//                        .field("view","100")
//                        .field("tag","a,b,c,d,e,f")
//                        .field("date",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time))
//                        .endObject())
//                .get();
        try {
            XContentBuilder xContentBuilder = XContentFactory.jsonBuilder().startObject();
            if (jsonObject!=null){
                Set<String> keys = jsonObject.keySet();
                Iterator<String> iterator = keys.iterator();
                while (iterator.hasNext()){
                    xContentBuilder.field(iterator.next(),jsonObject.get(iterator.next()));
                }
            }
            xContentBuilder.endObject();
            IndexResponse response = client.prepareIndex(article, content).setSource(xContentBuilder).get();
            System.out.println("添加索引成功,版本号："+response.getVersion());
        } catch (IOException e) {
            logger.error("addIndexAndDocument 异常",e);
        }
    }
}
