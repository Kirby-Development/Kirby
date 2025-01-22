package dev.kirby.config;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigInfo {

    String name() default "";
    Format format() default Format.YAML;

}
