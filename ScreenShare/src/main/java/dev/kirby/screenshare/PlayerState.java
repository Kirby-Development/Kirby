package dev.kirby.screenshare;

public enum PlayerState {
    SUSPECT,
    STAFF,
    DEBUG,
    NONE;

    public boolean isStaff() {
        return this == STAFF || this == DEBUG;
    }
}
