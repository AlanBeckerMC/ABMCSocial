package com.akutasan.abmcsocial.party.commands;


import com.akutasan.abmcsocial.ABMCSocial;
import com.akutasan.abmcsocial.manager.SubCommand;
import com.akutasan.abmcsocial.party.manager.P_Manager;
import com.akutasan.abmcsocial.party.manager.P_Player;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Objects;

public class P_Invite extends SubCommand {
    public P_Invite()
    {
        super("", "<Player>", "invite");
    }

    public void onCommand(ProxiedPlayer p, String[] args) {
        if (args.length == 0) {
            p.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§cPlease enter a valid §cName!"));
            return;
        }
        ProxiedPlayer pl = ProxyServer.getInstance().getPlayer(args[0]);
        if (pl == null) {
            p.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§e" + args[0] + " §cis not online!"));
            return;
        } else if (pl.getName().equals(p.getName())){
            p.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§cYou cannot invite yourself!"));
            return;
        }

        P_Player party;
        if ((P_Manager.getParty(p) != null) && (!Objects.requireNonNull(P_Manager.getParty(p)).isLeader(p))) {
            p.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§cYou are not the §cParty leader!"));
            return;
        } else if ((P_Manager.getParty(p) != null) && (Objects.requireNonNull(P_Manager.getParty(p)).hasRequest(pl))){
            p.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§cThe player has already been invited to your Party!"));
            return;
        } else if (P_Manager.getParty(pl) != null) {
            p.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§cThe player is §calready in another Party!"));
            return;
        }

        if (P_Manager.getParty(p) != null) {
            party = P_Manager.getParty(p);
            assert party != null;
            if (party.isInParty(pl)) {
                p.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§cThe player is §calready in your Party!"));
                return;
            }
            p.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§aYou have invited §6" + P_Player.getRankCol(pl) + pl + " §ainto your Party!"));
            party.invite(pl);
        } else {
            P_Manager.createParty(p);
            p.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§aYou have invited §6" + P_Player.getRankCol(pl) + pl + " §ainto your Party!"));
            party = P_Manager.getParty(p);
            assert party != null;
            party.invite(pl);
        }
    }
}