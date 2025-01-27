package dev.kirby.screenshare.config;

import dev.kirby.config.ConfigInfo;
import dev.kirby.config.License;
import dev.kirby.screenshare.PlayerState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigInfo
public class Config extends License {
    private String format = "%tag% %name% %message%";
    private Tags tags = new Tags();

    private int port = 6990;

    private String remainingTime = "%time%";


    public String getTag(PlayerState playerState) {
        return switch (playerState){
            case STAFF -> tags.staff;
            case SUSPECT -> tags.suspect;
            case DEBUG -> tags.debug;
            case null, default -> tags.none;
        };
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Tags {
        private String staff = "staff";
        private String suspect = "suspect";
        private String debug = "debug";
        private String none = "none";
    }
}
