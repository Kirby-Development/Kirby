package dev.kirby.license;

import dev.kirby.packet.Status;

import java.util.UUID;

public class LicenseChecker {

    private static final LicenseChecker INSTANCE = new LicenseChecker();

    public static LicenseChecker get() {
        return INSTANCE;
    }

    public Status check(String license, UUID uuid) {
        //todo check
        System.out.println("license: " + license);
        System.out.println("uuid: " + uuid);
        return Status.VALID;
    }
}
