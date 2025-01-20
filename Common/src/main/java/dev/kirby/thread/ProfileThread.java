package dev.kirby.thread;

import lombok.Getter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ProfileThread {
    private final ScheduledExecutorService thread = Executors.newScheduledThreadPool(1);

    @Getter
    private int profileCount;

    public void execute(Runnable runnable) {
        if (this.thread.isShutdown()) return;
        this.thread.execute(runnable);
    }

    public void executeWithDelay(Runnable runnable, long delay, TimeUnit unit) {
        if (this.thread.isShutdown()) return;
        this.thread.schedule(runnable, delay, unit);
    }

    public void executeRepeatingTask(Runnable runnable, long initialDelay, long period, TimeUnit unit) {
        if (this.thread.isShutdown()) return;
        this.thread.scheduleAtFixedRate(runnable, initialDelay, period, unit);
    }

    public ProfileThread incrementAndGet() {
        this.profileCount++;
        return this;
    }

    public void decrement() {
        this.profileCount--;
    }

    public ProfileThread shutdownThread() {
        this.thread.shutdownNow();
        return this;
    }
}
