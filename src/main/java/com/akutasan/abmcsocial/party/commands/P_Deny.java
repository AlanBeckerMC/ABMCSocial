package com.akutasan.abmcsocial.party.commands;

import com.akutasan.abmcsocial.ABMCSocial;
import com.akutasan.abmcsocial.manager.SubCommand;
import com.akutasan.abmcsocial.party.manager.P_Manager;
import com.akutasan.abmcsocial.party.manager.P_Player;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class P_Deny extends SubCommand
{
    public P_Deny()
    {
        super("", "<Player>", "deny");
    }

    public void onCommand(ProxiedPlayer p, String[] args) {
        if (args.length == 0){
            p.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§cPlease enter a valid Name!"));
            return;
        }
        if (P_Manager.getParty(p) != null){
            p.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§cYou are already §cin a Party!"));
            return;
        }
        ProxiedPlayer pl = ProxyServer.getInstance().getPlayer(args[0]);
        if (pl == null){
            p.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§cThis player is not online!"));
            return;
        }
        if (P_Manager.getParty(pl) == null){
            p.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§cThe player §e" + args[0] + " §cdoes not have a Party!"));
            return;
        }
        P_Player party = P_Manager.getParty(pl);
        assert party != null;
        if (!party.hasRequest(p)){
            p.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§cYou are not invited to this party!"));
            return;
        }
        party.removeInvite(p);
        p.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§cYou declined the invite of §e" + P_Player.getRankCol(party.getLeader()) + party.getLeader() + " §c!"));
        pl.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§e" + P_Player.getRankCol(p) + p.getName() + " §cdeclined your invite!"));
        if (party.getPlayers().size() == 1) {
            party.removePlayer(pl);
        }
    }
}
