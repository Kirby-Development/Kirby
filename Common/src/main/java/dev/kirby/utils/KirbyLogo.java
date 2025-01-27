package dev.kirby.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class KirbyLogo {

    public String get(String resource) {
        
        return """                
                %s  -  made by mk$weety
                """.formatted(resource);
    }

}
