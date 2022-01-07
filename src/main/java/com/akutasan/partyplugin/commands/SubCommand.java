package com.akutasan.partyplugin.commands;

import net.md_5.bungee.api.connection.ProxiedPlayer;

public abstract class SubCommand {
    private final String message;
    private final String usage;
    private final String[] aliases;

    public SubCommand(String message, String usage, String... aliases) {
        this.message = message;
        this.usage = usage;
        this.aliases = aliases;
    }

    public abstract void onCommand(ProxiedPlayer paramProxiedPlayer, String[] paramArrayOfString);

    public final String getMessage() {
        return this.message;
    }

    public final String getUsage() {
        return this.usage;
    }

    public final String[] getAliases() {
        return this.aliases;
    }
}
