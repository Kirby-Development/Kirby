package dev.kirby.thread;

import lombok.Getter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProfileThread {
    private final ExecutorService thread = Executors.newCachedThreadPool();

    @Getter
    private int profileCount;

    public void execute(Runnable runnable) {
        if (this.thread.isShutdown()) return;
        this.thread.execute(runnable);
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
