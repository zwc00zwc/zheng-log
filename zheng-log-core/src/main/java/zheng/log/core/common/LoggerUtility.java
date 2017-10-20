package zheng.log.core.common;

/**
 * Created by alan.zheng on 2017/10/16.
 */
public class LoggerUtility {
    private static ThreadLocal<String> threadLocal=new ThreadLocal<String>();

    private static ThreadLocal<String> requestIpThread=new ThreadLocal<String>();

    public static String getSessionId(){
        return threadLocal.get();
    }

    public static void changeSessionId(String SessionId){
        threadLocal.set(SessionId);
    }

    public static String getRequestIp(){
        return requestIpThread.get();
    }

    public static void changeRequestIp(String requestIp){
        requestIpThread.set(requestIp);
    }
}
