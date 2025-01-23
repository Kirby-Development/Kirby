package dev.kirby.resources;

import dev.kirby.thread.ProfileThread;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@RequiredArgsConstructor
public final class Connection {
    private final String licenseId;

    private final ProfileThread profileThread;

    private String ip;

    private boolean valid = false;
    private final AtomicInteger attempts = new AtomicInteger(0);

    public void ip(@NotNull String ip) {
        if (this.ip != null&& !this.ip.equals(ip)) reset();
        this.ip = ip;
    }

    public void increase(int maxAttempts) {
        attempts.set(Math.min(maxAttempts, attempts.get() + 1));
        profileThread.executeWithDelay(() -> attempts.set(Math.max(attempts.get() - 1, 0)), 5, TimeUnit.MINUTES);
    }

    public void reset() {
        attempts.set(0);
    }

    public void valid() {
        valid = true;
    }


}
