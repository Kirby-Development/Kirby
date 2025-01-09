package dev.kirby.checker.hwid;

import dev.kirby.ServerLauncher;

public class HwidCalculator {

    private final SecureGenerator generator;
    private HwidCalculator(ServerLauncher launcher) {
        generator = launcher.getGenerator();
    }

    private static HwidCalculator INSTANCE;

    public static HwidCalculator get(ServerLauncher serverLauncher) {
        if (INSTANCE == null) INSTANCE = new HwidCalculator(serverLauncher);
        return INSTANCE;
    }

    public String calculate(String... args) {
        String[] hashes = new String[args.length];
        for (int i = 0; i < args.length; i++) hashes[i] = args[i] + Salt.get(i);
        return generator.generateSecureID(hashes);
    }

}
