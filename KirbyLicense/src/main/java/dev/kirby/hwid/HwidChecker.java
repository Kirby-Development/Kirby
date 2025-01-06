package dev.kirby.hwid;

import dev.kirby.packet.Status;

public class HwidChecker {

    private static final HwidChecker INSTANCE = new HwidChecker();

    public static HwidChecker get() {
        return INSTANCE;
    }

    public Status check(String hwid) {
        //todo check
        System.out.println("hwid: " + hwid);
        return Status.VALID;
    }
}
