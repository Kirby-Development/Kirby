package dev.kirby.checker.hwid;

import dev.kirby.utils.Utils;
import lombok.SneakyThrows;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.zip.CRC32;

public class SecureGenerator {

    private final static String ALGORITHM = "HmacSHA256";
    private final String key;

    public SecureGenerator(String key) {
        this.key = key;
    }

    @SneakyThrows
    public String generateSecureID(String[]... datas) {
        final CRC32 crc = new CRC32();
        for (String[] data : datas) {
            byte[] bytes = generateBytes(data);
            crc.update(bytes);
        }
        return Long.toHexString(crc.getValue());
    }

    public byte[] generateBytes(String... input) throws Exception {
        Mac hmacSha256 = Mac.getInstance(ALGORITHM);
        SecretKeySpec keySpec = new SecretKeySpec(Utils.getBytes(key, Salt.get(key.length())), ALGORITHM);
        hmacSha256.init(keySpec);
        return hmacSha256.doFinal(Utils.getBytes(input));
    }

}
