package dev.kirby.checker.hwid;

import java.util.Arrays;
import java.util.Objects;
import java.util.zip.CRC32;

public class HwidCalculator {

    private HwidCalculator() {}

    private static final HwidCalculator INSTANCE = new HwidCalculator();

    public static HwidCalculator get() {
        return INSTANCE;
    }

    public String calculate(String... args) {
        String[] hashes = new String[args.length];
        for (int i = 0; i < args.length; i++) hashes[i] = args[i] + Salt.get(i);
        return hash(hashes);
    }

    private String hash(String... data) {
        final CRC32 crc = new CRC32();
        Arrays.stream(data).filter(Objects::nonNull).map(String::getBytes).forEach(crc::update);
        String hexString = Long.toHexString(crc.getValue());
        return hexString.substring(0, hexString.length() / 2) + "LUCKY" + hexString.substring(hexString.length() / 2);
    }

}
