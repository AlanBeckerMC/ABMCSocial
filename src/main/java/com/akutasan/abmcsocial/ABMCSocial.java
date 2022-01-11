package com.akutasan.abmcsocial;

import com.akutasan.abmcsocial.friends.commands.F_Command;
import com.akutasan.abmcsocial.manager.MySQL;
import com.akutasan.abmcsocial.party.Listener.P_ServerDisconnect;
import com.akutasan.abmcsocial.party.Listener.P_ServerSwitch;
import com.akutasan.abmcsocial.party.commands.P_Chat;
import com.akutasan.abmcsocial.party.commands.P_Command;
import com.akutasan.abmcsocial.manager.FileManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

import java.io.File;

public class ABMCSocial extends Plugin {

    private static ABMCSocial instance;
    public static String partyprefix;
    public static String friendprefix;
    private MySQL mysql;
    private FileManager fileManager;

    final String pluginname = getDescription().getName();
    final String partyname = "party.yml";
    final String mysqlname = "mysql.yml";

    public void onEnable() {
        instance = this;
        fetchClasses();

        // Create new Config folder
        if (!getDataFolder().exists()) getDataFolder().mkdir();

        // Load all configs
        loadPartyConfig();
        loadMySQLconfig();
        loadFriendsConfig();

        // Register commands
        getProxy().getPluginManager().registerCommand(this, new P_Command());
        getProxy().getPluginManager().registerCommand(this, new P_Chat());
        getProxy().getPluginManager().registerCommand(this, new F_Command());

        // Register Listeners
        getProxy().getPluginManager().registerListener(this, new P_ServerSwitch());
        getProxy().getPluginManager().registerListener(this, new P_ServerDisconnect());

        getLogger().info(ChatColor.GREEN +" successfully activated!");
    }

    public void fetchClasses(){
        mysql =  new MySQL(this);
        fileManager = new FileManager(this);
    }

    public void loadPartyConfig(){
        // Create party file
        if (!fileManager.exists(partyname, pluginname)){
            File file = fileManager.createNewFile(partyname ,pluginname);

            Configuration cfg = fileManager.getConfiguration(partyname, pluginname);
            cfg.set("Prefix","§f[§c§lABMC§f] ");
            fileManager.saveFile(file,cfg);
        }
        Configuration cfg = fileManager.getConfiguration(partyname, pluginname);
        partyprefix = cfg.getString("Prefix");
    }

    public void loadMySQLconfig(){
        // Create MySQL File
        if (!fileManager.exists(mysqlname, pluginname)){
            File file = fileManager.createNewFile(mysqlname, pluginname);
            Configuration cfg = fileManager.getConfiguration(mysqlname, pluginname);
            cfg.set("Hostname","YOURHOST");
            cfg.set("Database","YOURDATABASE");
            cfg.set("Username","YOURUSERNAME");
            cfg.set("Password","YOURPASSWORD");

            fileManager.saveFile(file,cfg);
        }
        Configuration cfg = fileManager.getConfiguration(mysqlname, pluginname);

        getMySQL().username = cfg.getString("Username");
        getMySQL().host = cfg.getString("Host");
        getMySQL().database = cfg.getString("Database");
        getMySQL().password = cfg.getString("Password");

        getMySQL().connect();
    }

    public void loadFriendsConfig(){

    }

    public MySQL getMySQL(){
        return mysql;
    }

    public void onDisable(){
        getMySQL().close();
        getLogger().info(ChatColor.RED+" successfully deactivated!");
    }

    public static Plugin getInstance()
    {
        return instance;
    }
}
