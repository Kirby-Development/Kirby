package dev.kirby.screenshare.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import dev.kirby.screenshare.ServiceHelper;

public abstract class SSCommand implements SimpleCommand, ServiceHelper {
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
}
