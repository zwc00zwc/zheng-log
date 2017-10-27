package application.controller;

import application.model.Member;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zheng.log.core.common.HttpRequestClient;
import zheng.log.core.common.TransportClientManager;

/**
 * Created by alan.zheng on 2017/10/12.
 */
@RestController
@RequestMapping(value = "search")
public class ElasticSearchController {
    @Autowired
    private HttpRequestClient httpRequestClient;

    @Autowired
    private TransportClientManager transportClientManager;

    @RequestMapping(value = "put")
    String index(Long id){
        Member member = new Member();
        member.setId(id);
        member.setName("张三"+id+"");
        member.setAge(17);
        member.setAbout("这是张三"+id+"");
        String jsonStr = JSON.toJSONString(member);
        String result = httpRequestClient.doJsonPost("http://192.168.48.129:9200/elastic1/member/2",jsonStr);
        return result;
    }

    @RequestMapping(value = "put2")
    String index2(String index,String type, Long id){
        Member member = new Member();
        member.setId(1L);
        member.setName("李四"+id+"");
        member.setAge(17);
        member.setAbout("这是张三"+id+"");
        String jsonStr = JSON.toJSONString(member);
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        transportClientManager.addIndexAndDocument(index, type, id.toString(),jsonObject);
        return "index2";
    }

    @RequestMapping(value = "get")
    String index3(Long id){
        JSONObject jsonObject = transportClientManager.getIndexAndDocument("es1","member",id.toString());
        return jsonObject.toJSONString();
    }

    @RequestMapping(value = "delete")
    String index4(Long id){
        transportClientManager.deleteIndex("es1","member",id.toString());
        return "delete";
    }

    @RequestMapping(value = "index5")
    String index5(String index,String type){
        JSONArray jsonArray = transportClientManager.search(index,type);
        return jsonArray.toJSONString();
    }

    @RequestMapping(value = "index6")
    String index6(String index,String type){
        JSONArray jsonArray = transportClientManager.monitorLog(index,type);
        return jsonArray.toJSONString();
    }
}
