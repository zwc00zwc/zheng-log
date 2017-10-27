package application.config;

import application.job.MonitorLogJob;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;

/**
 * Created by alan.zheng on 2017/10/26.
 */
@Configuration
public class MonitorLogJobConfig {
    @Resource
    protected ZookeeperRegistryCenter zookeeperRegistryCenter;

//    @Resource
//    protected JobEventConfiguration jobEventConfiguration;

    @Bean(name = "monitorLogJob")
    public SimpleJob monitorLogJob() {
        return new MonitorLogJob();
    }

    @Bean(name = "monitorLogJobScheduler",initMethod = "init")
    public JobScheduler monitorLogJobScheduler(@Value("${monitorLogJob.cron}") String cron,@Value("${monitorLogJob.shardingTotalCount}") Integer shardingTotalCount,
                                               @Value("${monitorLogJob.description}") String description,final SimpleJob monitorLogJob) {
        return new SpringJobScheduler(monitorLogJob, zookeeperRegistryCenter,
                getLiteJobConfiguration(monitorLogJob.getClass(), cron, shardingTotalCount, description));
    }

    protected LiteJobConfiguration getLiteJobConfiguration(final Class<? extends SimpleJob> jobClass, final String cron, final int shardingTotalCount, final String description) {
        String convert = null;
        try {
            convert=new String(description.getBytes("ISO-8859-1"),"gbk");
        } catch (UnsupportedEncodingException e) {
            //转换失败就失败吧
        }
        return LiteJobConfiguration.newBuilder(new SimpleJobConfiguration(JobCoreConfiguration.newBuilder(
                jobClass.getName(), cron, shardingTotalCount).description(convert).build(), jobClass.getCanonicalName())).overwrite(true).build();
    }
}
