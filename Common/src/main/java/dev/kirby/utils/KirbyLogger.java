package dev.kirby.utils;

import dev.kirby.packet.text.BroadCastPacket;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.StringJoiner;

@Getter
public class KirbyLogger {

    protected final Logger LOGGER;
    protected final String name;

    @Setter
    private boolean debug = false;

    public KirbyLogger(String name) {
        this.name = name;
        LOGGER = LogManager.getLogger(name);
    }

    public void log(Level level, Object... input) {
        LOGGER.log(level, getMessage(input));
    }

    public String getMessage(Object[] message) {
        StringJoiner joiner = new StringJoiner(" ");
        for (Object o : message) {
            joiner.add(switch (o) {
                case String s -> s;
                case Exception e -> e.getMessage();
                case Throwable e -> e.getMessage();
                case null -> "";
                default -> o.toString();
            });
        }
        return joiner.toString();
    }

    public void info(Object... input) {
        log(Level.INFO, input);
    }

    public void warn(Object... input) {
        log(Level.WARN, input);
    }

    public void error(Object... input) {
        log(Level.ERROR, input);
    }

    public void debug(Object... input) {
        if (!debug) return;
        log(Level.DEBUG, input);
    }

    public void log(BroadCastPacket.LogLevel level, String format, Object... input) {
        String message = format.formatted(input);
        switch (level) {
            case ERROR -> error(message);
            case WARN -> warn(message);
            case DEBUG -> debug(message);
            case INFO -> info(message);
        }
    }

}
