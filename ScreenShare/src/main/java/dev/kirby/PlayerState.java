package dev.kirby;

public enum PlayerState {
    SUSPECT,
    STAFF,
    DEBUG,
    NONE;

    public boolean isStaff() {
        return this == STAFF || this == DEBUG;
    }
}
