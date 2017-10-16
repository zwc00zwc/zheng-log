package application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zheng.kafka.core.KafkaProducerManager;

/**
 * Created by alan.zheng on 2017/10/16.
 */
@RestController
@RequestMapping(value = "/kafka")
public class KafkaController {

    @Autowired
    private KafkaProducerManager kafkaProducerManager;

    @RequestMapping(value = "/index")
    String index(){
        kafkaProducerManager.send("first-topic","aaa","这是aaa");
        return "index";
    }
}
