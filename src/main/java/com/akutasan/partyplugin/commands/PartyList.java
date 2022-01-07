package com.akutasan.partyplugin.commands;


import com.akutasan.partyplugin.Party;
import com.akutasan.partyplugin.manager.PartyManager;
import com.akutasan.partyplugin.manager.PlayerParty;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Objects;

// List all members in party
public class PartyList extends SubCommand {
    public PartyList() {
        super("", "", "list");
    }

    public void onCommand(ProxiedPlayer p, String[] args) {
        if (PartyManager.getParty(p) == null)
        {
            p.sendMessage(new TextComponent(Party.partyprefix + "§cYou are not §cin a §cParty!"));
            return;
        }
        PlayerParty party = PartyManager.getParty(p);
        assert party != null;
        int psize = party.getPlayers().size() + 1;
        p.sendMessage(new TextComponent("§8§m---------§7[§5"+party.getLeader().getName()+"'s Party §6(" + psize + ")§7]§8§m---------"));
        p.sendMessage(new TextComponent("§dParty Leader: §6" + PlayerParty.getRankCol(party.getLeader()) + party.getLeader().getName()));
        if (!party.getPlayers().isEmpty()) {
            p.sendMessage(new TextComponent("§dParty Members: §e" + getPl(p)));
        } else {
            p.sendMessage(new TextComponent("§dParty Members: §8None"));
        }
        p.sendMessage(new TextComponent("§dOnline on §a" + party.getLeader().getServer().getInfo().getName()));
        p.sendMessage(new TextComponent("§8§m-----§8§m-----§8§m-----§8§m----§8§m-----§8§m----§8§m----"));
    }

    public String getPl(ProxiedPlayer p){
        return Objects.requireNonNull(PartyManager.getParty(p)).getPlayers().toString()
                .replace("[", "")
                .replace("]", "")
                .trim();
    }
}
