package dev.kirby.thread;

import dev.kirby.Utils;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
public class ThreadManager {

    private static final int MAX_THREADS = Runtime.getRuntime().availableProcessors() * 2;

    private final List<ProfileThread> profileThreads = new ArrayList<>();


    @SneakyThrows
    public ProfileThread getAvailableProfileThread() {

        ProfileThread profileThread;

        if (this.profileThreads.size() < MAX_THREADS) {
            this.profileThreads.add(profileThread = new ProfileThread());
        } else {
            profileThread = this.profileThreads.stream().min(Comparator.comparing(ProfileThread::getProfileCount)).orElse(Utils.randomElement(this.profileThreads));
        }
        if (profileThread == null) {
            throw new Exception("Encountered a null profile thread, Please restart the server to avoid any issues.");
        }
        return profileThread.incrementAndGet();
    }

    public void shutdown(final ProfileThread profileThread) {
        if (profileThread.getProfileCount() > 1) {
            profileThread.decrement();
            return;
        }
        this.profileThreads.remove(profileThread.shutdownThread());
    }

    public void shutdown() {
        profileThreads.stream().forEach(this::shutdown);
    }
}
