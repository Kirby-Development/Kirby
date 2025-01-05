package dev.kirby.api.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.TimeUnit;

@UtilityClass
public class Utils {
    public boolean maikol(String string) {
        return string == null || string.isBlank();
    }

    public String fanculoMaikol(String string, String fallback) {
        return maikol(string) ? fallback : string;
    }


    @UtilityClass
    public static final class EnumUtil {

        public <E extends Enum<E>> E getByName(final String name, Class<E> clazz) {
            return clazz.cast(Arrays.stream(clazz.getEnumConstants()).filter(type -> type.name().equalsIgnoreCase(name)).findFirst().orElse(null));
        }
    }

    @UtilityClass
    public class Time {
        public String format(int seconds) {
            long days = TimeUnit.SECONDS.toDays(seconds);
            long hours = TimeUnit.SECONDS.toHours(seconds - TimeUnit.DAYS.toSeconds(days));
            long minutes = TimeUnit.SECONDS.toMinutes(seconds - TimeUnit.DAYS.toSeconds(days) - TimeUnit.HOURS.toSeconds(hours));
            long remainingSeconds = seconds - TimeUnit.DAYS.toSeconds(days) - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.MINUTES.toSeconds(minutes);

            return format(days, hours, minutes, remainingSeconds);
        }

        public String format(long millis) {
            long days = TimeUnit.MILLISECONDS.toDays(millis);
            long hours = TimeUnit.MILLISECONDS.toHours(millis - TimeUnit.DAYS.toMillis(days));
            long minutes = TimeUnit.MILLISECONDS.toMinutes(millis - TimeUnit.DAYS.toMillis(days) - TimeUnit.HOURS.toMillis(hours));
            long seconds = TimeUnit.MILLISECONDS.toSeconds(millis - TimeUnit.DAYS.toMillis(days) - TimeUnit.HOURS.toMillis(hours) - TimeUnit.MINUTES.toMillis(minutes));

            return format(days, hours, minutes, seconds);
        }

        @NotNull
        private String format(long days, long hours, long minutes, long remainingSeconds) {
            if (minutes < 1) return String.format("%02ds", remainingSeconds);
            if (hours < 1) return String.format("%02dm %02ds", minutes, remainingSeconds);
            if (days < 1) return String.format("%02dh %02dm %02ds", hours, minutes, remainingSeconds);
            return String.format("%02dd %02dh %02dm %02ds", days, hours, minutes, remainingSeconds);
        }
    }


    @UtilityClass
    public class Command {

        public void execute(String command) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }
        public void execute(CommandSender sender, String command) {
            Bukkit.dispatchCommand(sender, command);
        }

    }

    @NotNull
    public List<String> onlineNames() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
    }

    public void safeGive(Player p, ItemStack giveItem) {
        int amount = giveItem.getAmount();
        giveItem.setAmount(1);
        for (int i = 0; i < amount; i++) {
            p.getInventory().addItem(giveItem);
        }
    }

    public int freeSlots(Player p) {
        return (int) Arrays.stream(p.getInventory().getStorageContents()).filter(Objects::isNull).count();
    }

    public double round(final double value, final int digits) {
        int scale = 1;
        for (int i = 0; i < digits; i++) {
            scale *= 10;
        }
        return Math.round(value * scale) / (double) scale;
    }

    public double round(final double value) {
        return Math.round(value * 10d) / 10d;
    }


    public <E> E randomElement(final Collection<? extends E> collection) {
        if (collection.isEmpty()) return null;
        int index = new Random().nextInt(collection.size());
        if (collection instanceof List)
            return ((List<? extends E>) collection).get(index);
        Iterator<? extends E> iter = collection.iterator();
        for (int i = 0; i < index; i++) iter.next();
        return iter.next();

    }

    public void addPermission(UUID userUuid, String permission) {
        //LuckPermsProvider.get().getUserManager().modifyUser(userUuid, user -> user.data().add(Node.builder(permission).build()));
    }
}
