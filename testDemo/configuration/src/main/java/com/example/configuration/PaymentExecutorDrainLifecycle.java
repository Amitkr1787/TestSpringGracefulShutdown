package com.example.configuration;

import java.time.Duration;
import java.time.Instant;
import org.springframework.context.SmartLifecycle;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Runs before {@code WebServerGracefulShutdownLifecycle} (Spring Boot 3.x uses phase
 * {@code Integer.MAX_VALUE - 1024} for Tomcat graceful shutdown). Tomcat only waits for
 * servlet threads; {@code @Async} work on this pool is included by blocking here until the
 * pool is idle, so Tomcat's "Graceful shutdown complete" runs after in-flight payments finish.
 */
final class PaymentExecutorDrainLifecycle implements SmartLifecycle {

    private final ThreadPoolTaskExecutor paymentExecutor;
    private final Duration drainTimeout;
    private volatile boolean running;

    PaymentExecutorDrainLifecycle(ThreadPoolTaskExecutor paymentExecutor, Duration drainTimeout) {
        this.paymentExecutor = paymentExecutor;
        this.drainTimeout = drainTimeout;
    }

    @Override
    public void start() {
        running = true;
    }

    @Override
    public void stop() {
        running = false;
    }

    @Override
    public void stop(Runnable callback) {
        running = false;
        Instant deadline = Instant.now().plus(drainTimeout);
        while (Instant.now().isBefore(deadline)) {
            if (paymentExecutor.getActiveCount() == 0
                    && paymentExecutor.getThreadPoolExecutor().getQueue().isEmpty()) {
                break;
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        callback.run();
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }
}
