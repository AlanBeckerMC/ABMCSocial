package com.akutasan.abmcsocial.party.commands;

import com.akutasan.abmcsocial.ABMCSocial;
import com.akutasan.abmcsocial.manager.SubCommand;
import com.akutasan.abmcsocial.party.manager.P_Manager;
import com.akutasan.abmcsocial.party.manager.P_Player;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class P_Accept extends SubCommand
{
    public P_Accept()
    {
        super("", "<Player>", "accept");
    }

    public void onCommand(ProxiedPlayer p, String[] args){
        if (args.length == 0){
            p.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§cPlease enter a valid §cName!"));
            return;
        }
        if (P_Manager.getParty(p) != null){
            p.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§cYou are already in a Party!."));
            return;
        }
        ProxiedPlayer pl = ProxyServer.getInstance().getPlayer(args[0]);
        if (pl == null){
            p.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§e" + args[0] + " §cis not online!"));
            return;
        }
        if (P_Manager.getParty(pl) == null)        {
            p.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§cThe player " + args[0] + " §cdoesn't have an active Party!"));
            return;
        }
        P_Player party = P_Manager.getParty(pl);
        assert party != null;
        if (!party.hasRequest(p)) {
            p.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§cYou don't have an §cactive invite from §6" + args[0] + " §c!"));
        }
        if (party.isInParty(p)){
            p.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§cYou are already in a Party!"));
            return;
        }
        if (party.addPlayer(p)){
            for (ProxiedPlayer pp : party.getPlayers()) {
                pp.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§6" + P_Player.getRankCol(p) + p + " §ejoined the Party§e!"));
            }
            party.getLeader().sendMessage(new TextComponent(ABMCSocial.partyprefix + "§6" + P_Player.getRankCol(p) + p + " §ejoined the Party!"));
            party.removeInvite(p);
        }
    }
}

