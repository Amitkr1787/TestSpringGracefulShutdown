package com.example.configuration;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ExecutorConfig {

    @Bean(name = "paymentExecutor")
    public ThreadPoolTaskExecutor paymentExecutor(Environment env) {
        int awaitSeconds = (int)
                parseDrainTimeout(env.getProperty("spring.lifecycle.timeout-per-shutdown-phase", "40s"))
                        .toSeconds();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("Payment-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(awaitSeconds);
        executor.initialize();
        return executor;
    }

    /**
     * Drains {@code paymentExecutor} during shutdown before Tomcat reports graceful shutdown
     * complete (higher {@link org.springframework.context.SmartLifecycle#getPhase} than web server).
     */
    @Bean
    public PaymentExecutorDrainLifecycle paymentExecutorDrainLifecycle(
            @Qualifier("paymentExecutor") ThreadPoolTaskExecutor paymentExecutor, Environment env) {
        return new PaymentExecutorDrainLifecycle(
                paymentExecutor, parseDrainTimeout(env.getProperty("spring.lifecycle.timeout-per-shutdown-phase", "40s")));
    }

    private static Duration parseDrainTimeout(String raw) {
        String s = raw.trim().toLowerCase();
        if (s.endsWith("ms")) {
            return Duration.ofMillis(Long.parseLong(s.substring(0, s.length() - 2)));
        }
        if (s.endsWith("s")) {
            return Duration.ofSeconds(Long.parseLong(s.substring(0, s.length() - 1)));
        }
        if (s.endsWith("m")) {
            return Duration.ofMinutes(Long.parseLong(s.substring(0, s.length() - 1)));
        }
        return Duration.ofSeconds(Long.parseLong(s));
    }
}
