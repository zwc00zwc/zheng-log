package application.controller;

import application.annotation.ServiceRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zheng.log.core.common.HttpRequestClient;
import zheng.log.core.common.LoggerUtility;

/**
 * Created by alan.zheng on 2017/10/12.
 */
@RestController
@RequestMapping(value = "/home")
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    @Autowired
    private HttpRequestClient httpRequestClient;
    @RequestMapping(value = "/index")
    @ServiceRecord(service = "")
    String index(){
        Integer aa = null;
        try {
            if (aa==1){

            }
        } catch (Exception e) {
            logger.error("测试异常",e);
        }
        logger.info("当前访问index页面");
        return "index";
    }

    @RequestMapping(value = "/index3")
    @ServiceRecord(service = "")
    String index3(){
        Integer aa = null;
        try {
            if (aa==1){

            }
        } catch (Exception e) {
            logger.error("index3测试异常",e);
        }
//        logger.info("当前访问index页面");
        return "index";
    }

    @RequestMapping(value = "/index2")
    String index2(){
        String result = httpRequestClient.doGet("http://localhost:8080/home/index");
        return result;
    }

    @ServiceRecord(service = "当前访问index4")
    @RequestMapping(value = "/index4")
    String index4(){
        return "index4";
    }
}
