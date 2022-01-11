package com.akutasan.abmcsocial.party.commands;

import com.akutasan.abmcsocial.ABMCSocial;
import com.akutasan.abmcsocial.manager.SubCommand;
import com.akutasan.abmcsocial.party.manager.P_Manager;
import com.akutasan.abmcsocial.party.manager.P_Player;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

// Leave Command
public class P_Leave extends SubCommand {
    public P_Leave()
    {
        super("", "", "leave");
    }

    public void onCommand(ProxiedPlayer p, String[] args) {
        P_Player party = P_Manager.getParty(p);
        if (P_Manager.getParty(p) == null) {
            p.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§cYou are not in a Party!"));
        } else {
            // Check for Party Owner leaving the party
            assert party != null;
            if (party.isLeader(p)) {
                if (party.getPlayers().size() == 1) {
                    for (ProxiedPlayer pp : party.getPlayers()) {
                        pp.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§cThe §cparty §cwas dissolved because the party owner §cleft §cthe §cParty!"));
                    }
                    party.removePlayer(p);
                    P_Manager.deleteParty(p);
                } else {
                    for (ProxiedPlayer pp : party.getPlayers()) {
                        pp.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§cThe §cparty §cwas dissolved because the party owner §cleft §cthe §cParty!"));
                        party.removePlayer(p);
                        party.removePlayer(pp);
                    }
                }
                p.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§cThe §cparty §cwas dissolved because you §cleft §cthe §cParty!"));
            } else {
                party.removePlayer(p);
                p.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§cYou left the party!"));
                for (ProxiedPlayer pp : party.getPlayers()) {
                    pp.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§e" + P_Player.getRankCol(p) + p.getName() + " §chas §cleft the §cParty §c!"));
                }
                party.getLeader().sendMessage(new TextComponent(ABMCSocial.partyprefix + "§e" + P_Player.getRankCol(p) + p.getName() + " §chas §cleft the §cParty §c!"));
            }
        }
    }
}
