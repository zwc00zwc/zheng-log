package zheng.log.core.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

/**
 * Created by alan.zheng on 2017/11/2.
 */
@Component
public class MailManager {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String Sender;

    public void sendMonitorMail(JSONArray jsonArray,String toMail) {
        try {
            StringBuilder sb = null;
            if (jsonArray!=null && jsonArray.size()>0){
                for (int i=0;i<jsonArray.size();i++){
                    JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                    int logCount = jsonObject.getIntValue("count");
                    if (logCount>10){
                        if (sb == null){
                            sb = new StringBuilder();
                            sb.append("<h1>日志监控报告详情</h1>");
                            sb.append("<ul>");
                        }
                        sb.append("<li><div>");
                        sb.append("<p style=\"color:#F00;display: inline-block\">" + jsonObject.getString("log") + "</p>");
                        sb.append("<p style=\"display: inline-block\">发生<span style=\"color:#F00\">" + logCount + "</span>次</p>");
                        sb.append("</div><div></div></li>");
                    }
                }
            }
            if (sb != null){
                sb.append("</ul>");
                MimeMessage message = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setFrom(Sender);
                helper.setTo(toMail);
                helper.setSubject("日志监控报告");
                helper.setText(sb.toString(), true);
                javaMailSender.send(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendHtmlMail() {
        MimeMessage message = null;
        try {
            message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(Sender);
            helper.setTo("zheng.wenchao@yrw.com");
            helper.setSubject("主题：HTML邮件");

            StringBuffer sb = new StringBuffer();
            sb.append("<h1>大标题-h1</h1>")
                    .append("<p style='color:#F00'>红色字</p>")
                    .append("<p style='text-align:right'>右对齐</p>");
            helper.setText(sb.toString(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        javaMailSender.send(message);
    }
}
