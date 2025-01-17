package dev.kirby.api.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class InvalidException extends RuntimeException {

    public InvalidException(String message) {
        super(message);
        StackTraceElement[] old = getStackTrace();
        StackTraceElement[] stackTrace;
        if (old.length > 0) stackTrace = new StackTraceElement[]{old[0]};
        else stackTrace = new StackTraceElement[0];
        setStackTrace(stackTrace);
    }

    public InvalidException(Type type) {
        this(type.getMessage());
    }

    @RequiredArgsConstructor
    @Getter
    public enum Type {
        SESSION("Invalid session!"),
        LICENSE("License not found in config.yml"),
        CONNECTION("You must be connected to the server first!"),
        ;

        private final String value;

        private final String name = name().substring(0, 1).toUpperCase() + name().substring(1).toLowerCase();

        public String getMessage() {
            return "[%s] %s".formatted(name, value);
        }
    }
}
