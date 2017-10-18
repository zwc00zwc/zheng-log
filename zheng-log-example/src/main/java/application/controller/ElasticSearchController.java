package application.controller;

import application.model.Member;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zheng.log.core.common.HttpRequestClient;

/**
 * Created by alan.zheng on 2017/10/12.
 */
@RestController
@RequestMapping(value = "search")
public class ElasticSearchController {
    @Autowired
    private HttpRequestClient httpRequestClient;

//    @Autowired
//    private TransportClientManager transportClientManager;

    @RequestMapping(value = "put")
    String index(){
        Member member = new Member();
        member.setId(1L);
        member.setName("张三");
        member.setAge(17);
        member.setAbout("这是张三");
        String jsonStr = JSON.toJSONString(member);
        String result = httpRequestClient.doJsonPost("http://192.168.48.129:9200/elastic1/member/1",jsonStr);
        return result;
    }

    @RequestMapping(value = "put2")
    String index2(){
        Member member = new Member();
        member.setId(1L);
        member.setName("张三");
        member.setAge(17);
        member.setAbout("这是张三");
        String jsonStr = JSON.toJSONString(member);
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
//        transportClientManager.addIndexAndDocument("es1","member",jsonObject);
        return "index2";
    }
}
