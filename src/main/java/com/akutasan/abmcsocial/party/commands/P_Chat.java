package com.akutasan.abmcsocial.party.commands;

import com.akutasan.abmcsocial.ABMCSocial;
import com.akutasan.abmcsocial.party.manager.P_Manager;
import com.akutasan.abmcsocial.party.manager.P_Player;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class P_Chat extends Command {
    public P_Chat()
    {
        super("pc");
    }

    public void execute(CommandSender sender, String[] args){
        if (args.length == 0){
            sender.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§c/p <Message>"));
            return;
        }
        ProxiedPlayer p = (ProxiedPlayer)sender;
        if (P_Manager.getParty(p) == null){
            p.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§cYou aren't in a §cParty!"));
            return;
        }
        P_Player party = P_Manager.getParty(p);
        StringBuilder msg = new StringBuilder();
        int j = args.length;
        int i = 0;
        while (i < j) {
            String s = args[i];
            msg.append("§7 ").append(s);
            i++;
        }
        assert party != null;
        for (ProxiedPlayer members : party.getPlayers()) {
            members.sendMessage(new TextComponent("§d§lParty §6" + P_Player.getRankCol(p) + p + " §7» §7" + msg));
        }
        party.getLeader().sendMessage(new TextComponent("§d§lParty §6" + P_Player.getRankCol(p) + p + " §7» §7" + msg));
    }
}