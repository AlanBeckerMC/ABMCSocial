package com.akutasan.abmcsocial.party.commands;


import com.akutasan.abmcsocial.ABMCSocial;
import com.akutasan.abmcsocial.manager.SubCommand;
import com.akutasan.abmcsocial.party.manager.P_Manager;
import com.akutasan.abmcsocial.party.manager.P_Player;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Objects;

// List all members in party
public class P_List extends SubCommand {
    public P_List() {
        super("", "", "list");
    }

    public void onCommand(ProxiedPlayer p, String[] args) {
        if (P_Manager.getParty(p) == null)
        {
            p.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§cYou are not §cin a §cParty!"));
            return;
        }
        P_Player party = P_Manager.getParty(p);
        assert party != null;
        int psize = party.getPlayers().size() + 1;
        p.sendMessage(new TextComponent("§8§m---------§7[§5"+party.getLeader().getName()+"'s Party §6(" + psize + ")§7]§8§m---------"));
        p.sendMessage(new TextComponent("§dParty Leader: §6" + P_Player.getRankCol(party.getLeader()) + party.getLeader().getName()));
        if (!party.getPlayers().isEmpty()) {
            p.sendMessage(new TextComponent("§dParty Members: §e" + getPl(p)));
        } else {
            p.sendMessage(new TextComponent("§dParty Members: §8None"));
        }
        p.sendMessage(new TextComponent("§dOnline on §a" + party.getLeader().getServer().getInfo().getName()));
        p.sendMessage(new TextComponent("§8§m-----§8§m-----§8§m-----§8§m----§8§m-----§8§m----§8§m----"));
    }

    public String getPl(ProxiedPlayer p){
        return Objects.requireNonNull(P_Manager.getParty(p)).getPlayers().toString()
                .replace("[", "")
                .replace("]", "")
                .trim();
    }
}
