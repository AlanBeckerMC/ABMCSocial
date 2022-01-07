package com.akutasan.partyplugin;

import com.akutasan.partyplugin.Listener.ServerDisconnect;
import com.akutasan.partyplugin.Listener.ServerSwitch;
import com.akutasan.partyplugin.commands.PartyChat;
import com.akutasan.partyplugin.commands.PartyCommand;
import com.akutasan.partyplugin.manager.FileManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

import java.io.File;

public class Party extends Plugin {

    private static Party instance;
    public static String partyprefix;

    public void onEnable() {
        instance = this;
        FileManager fileManager = new FileManager(this);

        // Create new Config file
        if (!getDataFolder().exists()) getDataFolder().mkdir();
        if (!fileManager.exists("config.yml","PartyPlugin")){
            File file = fileManager.createNewFile("config.yml","PartyPlugin");

            Configuration cfg = fileManager.getConfiguration("config.yml","PartyPlugin");
            cfg.set("Prefix","§f[§c§lABMC§f] ");
            fileManager.saveFile(file,cfg);
        }
        Configuration cfg = fileManager.getConfiguration("config.yml","PartyPlugin");
        partyprefix = cfg.getString("Prefix");

        // Register commands
        getProxy().getPluginManager().registerCommand(this, new PartyCommand());
        getProxy().getPluginManager().registerListener(this, new ServerSwitch());
        getProxy().getPluginManager().registerListener(this, new ServerDisconnect());
        getProxy().getPluginManager().registerCommand(this, new PartyChat());

        getLogger().info(ChatColor.GREEN +" successfully activated!");
    }

    public void onDisable(){
        getLogger().info(ChatColor.RED+" successfully deactivated!");
    }

    public static Plugin getInstance()
    {
        return instance;
    }
}
