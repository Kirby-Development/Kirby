package dev.kirby.screenshare.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.kirby.screenshare.player.SSManager;
import dev.kirby.screenshare.player.SSPlayer;
import dev.kirby.screenshare.utils.ServiceHelper;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class SSCommand implements SimpleCommand, ServiceHelper {

    private final SSManager manager;
    private final ProxyServer server;
    public SSCommand(ProxyServer server, SSManager manager) {
        this.server = server;
        this.manager = manager;
    }

    @Override
    public void execute(Invocation invocation) {
        String[] args = invocation.arguments();
        CommandSource source = invocation.source();

        if (source instanceof Player p) {
            execute(p, args);
            return;
        }

        execute(source, args);
    }

    private void execute(CommandSource source, String[] args) {}
    protected abstract void execute(Player p, String[] args);

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("kirby.screenshare." + invocation.alias());
    }


    protected @Nullable Result result(Player p, String[] args) {
        if (args.length == 0) {
            p.sendMessage(Component.text("not enouh args"));
            //todo not enouh args
            return null;
        }
        Optional<Player> optional = server.getPlayer(args[0]);
        if (optional.isEmpty()) {
            p.sendMessage(Component.text("player not found"));
            //todo player not found
            return null;
        }
        SSPlayer staff = manager.getProfile(p);
        if (staff == null) return null;
        Player target = optional.get();
        SSPlayer ssTarget = manager.getProfile(target);
        if (ssTarget == null) return null;
        return new Result(staff, target, ssTarget);
    }

    protected record Result(SSPlayer staff, Player target, SSPlayer ssTarget) {
    }
}
