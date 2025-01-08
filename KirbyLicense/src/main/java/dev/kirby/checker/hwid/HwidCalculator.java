package dev.kirby.checker.hwid;

import dev.kirby.ServerLauncher;
import lombok.SneakyThrows;

import java.util.zip.CRC32;

public class HwidCalculator {

    private final SecureUUIDGenerator uuidGenerator;
    private HwidCalculator(ServerLauncher launcher) {
        uuidGenerator = launcher.getSecureUUIDGenerator();
    }

    private static HwidCalculator INSTANCE;

    public static HwidCalculator get(ServerLauncher serverLauncher) {
        if (INSTANCE == null) INSTANCE = new HwidCalculator(serverLauncher);
        return INSTANCE;
    }

    public String calculate(String... args) {
        String[] hashes = new String[args.length];
        for (int i = 0; i < args.length; i++) hashes[i] = args[i] + Salt.get(i);
        return hash(hashes);
    }

    @SneakyThrows
    private String hash(String... data) {
        final CRC32 crc = new CRC32();
        for (String s : data) {
            if (s != null) {
                byte[] bytes = uuidGenerator.generateSecureBytes(s);
                crc.update(bytes);
            }
        }
        return Long.toHexString(crc.getValue());
    }

}
