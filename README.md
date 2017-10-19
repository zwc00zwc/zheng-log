# zheng-log
##日志服务##

项目结构
zheng-log-core            日志核心包
zheng-log-example         日志例子
zheng-log-kafka-consume   kafka消费者

日志服务采用kafka+logback+ElasticSearch+LogStash+Kinaba
业务日志和异常日志以session为维度进行归类

使用示例
logback.xml 增加
<appender name="kafkaLoggerAppender"  class="zheng.log.core.log.KafkaLoggerAppender">
    <servers>hostname1:9092,hostname2:9092,hostname3:9092</servers>
    <topic>***-log</topic>
</appender>
进行异常日志的收集

业务日志使用注解进行收集
@ServiceRecord(service = "日志内容")

##效果截图##
<p>
    <img src="https://github.com/zwc00zwc/zheng-log/blob/master/doc/1.png" style="float:none;"/>
</p>
