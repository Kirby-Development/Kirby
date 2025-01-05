package dev.kirby.api.util;

import lombok.Getter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.apache.logging.log4j.Level.INFO;

@Getter
public class KirbyLogger {

    protected final Logger LOGGER;
    protected final String name;

    public KirbyLogger(String name) {
        this.name = name;
        LOGGER = LogManager.getLogger(name);
    }

    public void log(Level level, Object... message) {
        LOGGER.log(level, Arrays.stream(message).map(o -> switch (o) {
            case Exception e -> e.getMessage();
            case Throwable e -> e.getMessage();
            case null -> "";
            default -> o.toString();
        }).collect(Collectors.joining(" ")));
    }

    public void info(Object... donna) {
        log(INFO, donna);
    }

    public void warn(Object... donna) {
        log(Level.WARN, donna);
    }

    public void error(Object... donna) {
        log(Level.ERROR, donna);
    }

    public void debug(Object... donna) {
        log(Level.DEBUG, donna);
    }

}
