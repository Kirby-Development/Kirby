package dev.kirby.screenshare.configuration;

import dev.kirby.config.ConfigInfo;
import dev.kirby.config.License;
import dev.kirby.screenshare.utils.ServerUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@ConfigInfo
public class Config extends License {

    private Servers servers = new Servers();
    private Messages messages = new Messages();
    private Buttons buttons = new Buttons();
    private Ban ban = new Ban();

    @Data
    @NoArgsConstructor
    public static class Servers {
        private String ss = "ss";
        private String hub = "hub";
    }

    @Data
    @NoArgsConstructor
    public static class Messages {
        private String a = "a";
        private String notInSS = "%player% is not in ss";
        private String alreadyInSS = "%player% is already in a ss";
        private String notArgs = "Not enough args";
        private String playerNotFound = "Player %player% not found";

        public @NotNull Component getNotInSS(String username) {
            return ServerUtils.component(notInSS.replace("%player%", username));
        }

        public @NotNull Component getAlreadyInSS(String username) {
            return ServerUtils.component(alreadyInSS.replace("%player%", username));
        }

        public @NotNull Component getNotEnoughArgs() {
            return ServerUtils.component(notArgs);
        }

        public @NotNull Component getPlayerNotFound(String name) {
            return ServerUtils.component(playerNotFound.replace("%player%", name));
        }
    }

    @Data
    @NoArgsConstructor
    public static class Buttons {
        private Button admission = new Button();
        private Button cheating = new Button();
        private Button refuse = new Button();
        private Button clear = new Button();

        private final List<Button> buttons = new ArrayList<>(List.of(admission, cheating, refuse, clear));

        @Data
        @NoArgsConstructor
        public static class Button {
            private String duration = "";
            private String text = "";
            private String hover = "";

            public Component getText() {
                return ServerUtils.component(text);
            }

            public Component getHover() {
                return ServerUtils.component(hover);
            }
        }
    }

    @Data
    @NoArgsConstructor
    public static class Ban {
        private Who who = Who.Staff;
        private String command = "ban %duration% %player% %cause%";

        public enum Who {
            Staff, Console;

            public boolean isStaff() {
                return this == Staff;
            }
        }
    }
}

