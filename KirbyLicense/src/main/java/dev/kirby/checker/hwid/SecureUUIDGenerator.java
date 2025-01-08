package dev.kirby.checker.hwid;

import dev.kirby.Utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.util.UUID;

public class SecureUUIDGenerator {

    private final static String ALGORITHM = "HmacSHA256";
    private final String key;

    public SecureUUIDGenerator(String key) {
        this.key = key;
    }

    public UUID generateSecureUUID(String... input) throws Exception {
        byte[] hmacResult = generateSecureBytes(input);
        ByteBuffer byteBuffer = ByteBuffer.wrap(hmacResult);
        long mostSigBits = byteBuffer.getLong();
        long leastSigBits = byteBuffer.getLong();
        return new UUID(mostSigBits, leastSigBits);
    }

    public byte[] generateSecureBytes(String... input) throws Exception {
        Mac hmacSha256 = Mac.getInstance(ALGORITHM);
        SecretKeySpec keySpec = new SecretKeySpec(Utils.getBytes(key, Salt.get(key.length())), ALGORITHM);
        hmacSha256.init(keySpec);
        return hmacSha256.doFinal(Utils.getBytes(input));
    }

}
