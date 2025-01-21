package dev.kirby.thread;

import dev.kirby.utils.Utils;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
public class ThreadManager {

    private static final int MAX_THREADS = Runtime.getRuntime().availableProcessors() * 2;
    private static final Comparator<ProfileThread> comparator = Comparator.comparing(ProfileThread::getProfileCount);

    private final List<ProfileThread> profileThreads = new ArrayList<>();

    @SneakyThrows
    public ProfileThread getAvailableProfileThread() {
        ProfileThread profileThread;

        if (this.profileThreads.size() < MAX_THREADS) {
            this.profileThreads.add(profileThread = new ProfileThread());
        } else profileThread = this.profileThreads.stream().min(comparator).orElse(Utils.randomElement(this.profileThreads));
        if (profileThread == null)
            throw new Exception("Encountered a null profile thread, Please restart the server to avoid any issues.");

        return profileThread.incrementAndGet();
    }

    public void shutdown(final ProfileThread profileThread) {
        if (profileThread == null) return;
        if (profileThread.getProfileCount() > 1) {
            profileThread.decrement();
            return;
        }
        if (!this.profileThreads.contains(profileThread)) return;
        this.profileThreads.remove(profileThread.shutdown());
    }

    public void shutdown() {
        profileThreads.forEach(this::shutdown);
    }
}
