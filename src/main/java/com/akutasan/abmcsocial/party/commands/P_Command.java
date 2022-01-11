package com.akutasan.abmcsocial.party.commands;

import com.akutasan.abmcsocial.ABMCSocial;
import com.akutasan.abmcsocial.manager.SubCommand;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class P_Command extends Command {
    private final List<SubCommand> cmds = new ArrayList<>();

    public P_Command(){
        super("party", "", "p");
        this.cmds.add(new P_Invite());
        this.cmds.add(new P_Accept());
        this.cmds.add(new P_Deny());
        this.cmds.add(new P_List());
        this.cmds.add(new P_Leave());
        this.cmds.add(new P_Kick());
    }

    public void execute(CommandSender sender, String[] args){
        if (!(sender instanceof ProxiedPlayer)){
            sender.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§cThis is not a Console Command!"));
            return;
        }
        ProxiedPlayer p = (ProxiedPlayer)sender;
        if (args.length == 0 || args[0].equalsIgnoreCase("help")){
            for (SubCommand sc : this.cmds){
                TextComponent t = new TextComponent(ABMCSocial.partyprefix + "§e/party " + aliases(sc) + " " + sc.getUsage() + " §7" + sc.getMessage());
                t.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/party " + aliases(sc) + " "));
                p.sendMessage(t);
            }
            TextComponent t = new TextComponent(ABMCSocial.partyprefix + "§e/pc <Message>");
            t.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/p "));
            p.sendMessage(t);
            return;
        }
        SubCommand sc = getCommand(args[0]);
        if (sc == null){
            p.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§cThat is not a valid §ccommand!"));
            return;
        }
        Object a = new Vector<>(Arrays.asList(args));
        ((Vector)a).remove(0);
        args = (String[])((Vector)a).toArray(new String[0]);
        sc.onCommand(p, args);
    }

    private String aliases(SubCommand sc){
        StringBuilder fin = new StringBuilder();
        String[] arrstring = sc.getAliases();
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n)
        {
            String a = arrstring[n2];
            fin.append(a).append(" | ");
            n2++;
        }
        return fin.substring(0, fin.lastIndexOf(" | "));
    }

    private SubCommand getCommand(String name){
        int n2;
        for (SubCommand sc : this.cmds) {
            if (sc.getClass().getSimpleName().equalsIgnoreCase(name)) {
                return sc;
            }
            String[] arrstring = sc.getAliases();
            n2 = 0;
            String alias = arrstring[n2];
            if (alias.equalsIgnoreCase(name)) {
                return sc;
            }
        }
        return null;
    }
}