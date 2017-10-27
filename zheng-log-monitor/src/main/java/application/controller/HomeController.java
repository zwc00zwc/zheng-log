package application.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by alan.zheng on 2017/10/26.
 */
@RestController
@RequestMapping(value = "/home")
public class HomeController {
    @RequestMapping(value = "/index")
    String index(){
        return "index";
    }
}
