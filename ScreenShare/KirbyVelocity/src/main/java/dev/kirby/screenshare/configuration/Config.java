package dev.kirby.screenshare.configuration;

import com.velocitypowered.api.proxy.Player;
import dev.kirby.config.ConfigInfo;
import dev.kirby.config.Format;
import dev.kirby.config.License;
import dev.kirby.screenshare.utils.ServerUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@ConfigInfo(format = Format.YAML)
public class Config extends License {

    private Servers servers = new Servers();
    private Messages messages = new Messages();
    private Buttons buttons = new Buttons();
    private Ban ban = new Ban();
    private Titles titles = new Titles();

    @Data
    @NoArgsConstructor
    public static class Servers {
        private String ss = "ss";
        private String hub = "hub";
    }

    @Data
    @NoArgsConstructor
    public static class Messages {
        private String notInSS = "%player% is not in ss";
        private String alreadyInSS = "%player% is already in a ss";
        private String notArgs = "Not enough args";
        private String playerNotFound = "Player %player% not found";

        public @NotNull Component notInSS(String username) {
            return ServerUtils.component(notInSS.replace("%player%", username));
        }

        public @NotNull Component alreadyInSS(String username) {
            return ServerUtils.component(alreadyInSS.replace("%player%", username));
        }

        public @NotNull Component notEnoughArgs() {
            return ServerUtils.component(notArgs);
        }

        public @NotNull Component playerNotFound(String name) {
            return ServerUtils.component(playerNotFound.replace("%player%", name));
        }
    }

    @Data
    @NoArgsConstructor
    public static class Titles {
        private TitleState start = new TitleState();
        private TitleState end = new TitleState();

        @Data
        @NoArgsConstructor
        public static class TitleState {
            private StateMessage staff = new StateMessage();
            private StateMessage debug = new StateMessage();
            private StateMessage sus = new StateMessage();
        }
    }

    @Data
    @NoArgsConstructor
    public static class StateMessage {
        private Message message = new Message("message");
        private SSTitle title = new SSTitle();

        public void send(Player player, Message.Param... params) {
            if (message != null) player.sendMessage(message.toComponent(params));
            if (title != null) player.showTitle(title.toTitle(params));
        }


        @Data
        @NoArgsConstructor
        public static class SSTitle {
            private Message title = new Message("title"), subtitle = new Message("subtitle");
            private int in = 20, stay = 50, out = 20;

            public @NotNull Component title(Message.Param... params) {
                return title.toComponent(params);
            }

            public @NotNull Component subtitle(Message.Param... params) {
                return subtitle.toComponent(params);
            }

            public Title toTitle(Message.Param... params) {
                return Title.title(title(params), subtitle(params), Title.Times.times(Duration.ofMillis(in * 50L), Duration.ofMillis(stay * 50L), Duration.ofMillis(out * 50L)));
            }
        }
    }

    @Data
    @NoArgsConstructor
    public static class Buttons {
        private Button admission = new Button("7d", "Admission", "Click to Ban!");
        private Button cheating = new Button("14d", "Cheating", "Click to Ban!");
        private Button refuse = new Button("18d", "Refuse", "Click to Ban!");
        private Button clean = new Button("", "Clean", "Click to Leave!");

        private final List<Button> buttons = new ArrayList<>(List.of(admission, cheating, refuse));

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Button {
            private String duration = "";
            private Message text = new Message("text");
            private Message hover = new Message("hover");


            public Button(String duration, String text, String hover) {
                this.duration = duration;
                this.text = new Message(text);
                this.hover = new Message(hover);
            }

            public Component text(Message.Param... params) {
                return text.toComponent(params);
            }

            public Component hover(Message.Param... params) {
                return hover.toComponent(params);
            }
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Message {
        private String message = "message";

        public @NotNull Component toComponent(Param... params) {
            String result = getMessage();
            for (Param param : params) result = param.replace(result);
            return ServerUtils.component(result);
        }

        public record Param(String target, String value) {

            public String replace(String text) {
                return text.replace(target, value);
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
