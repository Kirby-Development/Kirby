package dev.kirby.api.util;

import lombok.Getter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.StringJoiner;

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
        StringJoiner joiner = new StringJoiner(" ");
        for (Object o : message) {
            //todo java 21
            String s;
            if (o instanceof Exception e) {
                s = e.getMessage();
            } else if (o instanceof Throwable e) {
                s =  e.getMessage();
            } else if (o == null) {
                s =  "";
            } else {
                s =  o.toString();
            }
            joiner.add(s);
        }
        LOGGER.log(level, joiner.toString());
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
