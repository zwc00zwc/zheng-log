package application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zheng.log.common.HttpRequestClient;

/**
 * Created by alan.zheng on 2017/10/12.
 */
@RestController
@RequestMapping(value = "/home")
public class HomeController {
    @Autowired
    private HttpRequestClient httpRequestClient;
    @RequestMapping(value = "/index")
    String index(){
        return "index";
    }

    @RequestMapping(value = "/index2")
    String index2(){
        String result = httpRequestClient.doGet("http://localhost:8080/home/index");
        return result;
    }
}
