package application.handler;

import application.annotation.ServiceRecord;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import zheng.log.core.common.LoggerUtility;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by alan.zheng on 2017/10/18.
 */
@Aspect
@Component
public class ServiceRecordHandler {
    @Pointcut("@annotation(application.annotation.ServiceRecord)")
    public void methodPointcut(){

    }

    @Before("@annotation(serviceRecord)")
    public void before(ServiceRecord serviceRecord){
        System.out.println("before方法");
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest().getSession();
        try {
            LoggerUtility.changeSessionId(session.getId());
        } catch (Exception e) {

        }
    }
}
