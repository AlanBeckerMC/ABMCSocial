package com.akutasan.partyplugin.commands;

import com.akutasan.partyplugin.Party;
import com.akutasan.partyplugin.manager.PartyManager;
import com.akutasan.partyplugin.manager.PlayerParty;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

// Leave Command
public class PartyLeave extends SubCommand {
    public PartyLeave()
    {
        super("", "", "leave");
    }

    public void onCommand(ProxiedPlayer p, String[] args) {
        PlayerParty party = PartyManager.getParty(p);
        if (PartyManager.getParty(p) == null) {
            p.sendMessage(new TextComponent(Party.partyprefix + "§cYou are not in a Party!"));
        } else {
            // Check for Party Owner leaving the party
            assert party != null;
            if (party.isLeader(p)) {
                if (party.getPlayers().size() == 1) {
                    for (ProxiedPlayer pp : party.getPlayers()) {
                        pp.sendMessage(new TextComponent(Party.partyprefix + "§cSince the party owner §cleft §cthe §cParty, §cthe §cparty §cwas dissolved!"));
                    }
                    party.removePlayer(p);
                    PartyManager.deleteParty(p);
                } else {
                    for (ProxiedPlayer pp : party.getPlayers()) {
                        pp.sendMessage(new TextComponent(Party.partyprefix + "§cSince the party owner §cleft §cthe §cParty, §cthe §cparty §cwas dissolved!"));
                        party.removePlayer(p);
                        party.removePlayer(pp);
                    }
                }
                p.sendMessage(new TextComponent(Party.partyprefix + "§cYou left the party §cand §cthe §cParty §cwas §cdissolved!"));
            } else {
                party.removePlayer(p);
                p.sendMessage(new TextComponent(Party.partyprefix + "§cYou left the party!"));
                for (ProxiedPlayer pp : party.getPlayers()) {
                    pp.sendMessage(new TextComponent(Party.partyprefix + "§e" + PlayerParty.getRankCol(p) + p.getName() + " §chas §cleft the §cParty §c!"));
                }
                party.getLeader().sendMessage(new TextComponent(Party.partyprefix + "§e" + PlayerParty.getRankCol(p) + p.getName() + " §chas §cleft the §cParty §c!"));
            }
        }
    }
}
