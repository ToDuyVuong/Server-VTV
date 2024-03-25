package hcmute.kltn.vtv.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "asyncExecutor")
    public ThreadPoolTaskExecutor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20); // Số lượng luồng cố định
        executor.setMaxPoolSize(100); // Số lượng luồng tối đa
        executor.setQueueCapacity(200); // Số lượng tác vụ trong hàng đợi đợi để xử lý
        executor.setThreadNamePrefix("Async-");
        executor.initialize();
        return executor;
    }
}