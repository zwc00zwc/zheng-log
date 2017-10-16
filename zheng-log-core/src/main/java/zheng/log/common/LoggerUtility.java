package zheng.log.common;

/**
 * Created by alan.zheng on 2017/10/16.
 */
public class LoggerUtility {
    private static ThreadLocal<String> threadLocal=new ThreadLocal<String>();

    public static String getLoggerId(){
        return threadLocal.get();
    }

    public static void changeLoggerId(String LoggerId){
        threadLocal.set(LoggerId);
    }
}
