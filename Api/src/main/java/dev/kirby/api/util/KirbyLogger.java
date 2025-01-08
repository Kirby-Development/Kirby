package dev.kirby.api.util;

import lombok.Getter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.StringJoiner;

@Getter
public class KirbyLogger {

    protected final Logger LOGGER;
    protected final String name;

    public KirbyLogger(String name) {
        this.name = name;
        LOGGER = LogManager.getLogger(name);
    }

    public String log(Level level, Object... message) {
        StringJoiner joiner = new StringJoiner(" ");
        for (Object o : message) {
            joiner.add(switch (o) {
                case Exception e -> e.getMessage();
                case Throwable e -> e.getMessage();
                case null -> "";
                default -> o.toString();
            });
        }
        String string = joiner.toString();
        LOGGER.log(level, string);
        return string;
    }

    public void info(Object... donna) {
        log(Level.INFO, donna);
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
