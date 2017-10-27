package application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zheng.log.core.common.WeixinManager;

/**
 * Created by alan.zheng on 2017/10/26.
 */
@RestController
@RequestMapping(value = "/home")
public class HomeController {

    @Autowired
    private WeixinManager weixinManager;

    @RequestMapping(value = "/index")
    String index(){
        //weixinManager.monitorNotice(1,"test");
        return "index";
    }
}
