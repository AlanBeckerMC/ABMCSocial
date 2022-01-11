package com.akutasan.abmcsocial.party.commands;

import com.akutasan.abmcsocial.ABMCSocial;
import com.akutasan.abmcsocial.manager.SubCommand;
import com.akutasan.abmcsocial.party.manager.P_Manager;
import com.akutasan.abmcsocial.party.manager.P_Player;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.concurrent.TimeUnit;

// Kick member from Party
public class P_Kick extends SubCommand {
    public P_Kick()
    {
        super("", "<Player>", "kick");
    }

    public void onCommand(ProxiedPlayer p, String[] args) {
        if (args.length == 0){
            p.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§cPlease enter a valid §cName!"));
            return;
        }
        if (P_Manager.getParty(p) == null)
        {
            p.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§cYou are not in §ca Party!"));
            return;
        }
        P_Player party = P_Manager.getParty(p);
        assert party != null;
        if (!party.isLeader(p))
        {
            p.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§cYou are not the §cParty §cleader!"));
            return;
        }
        ProxiedPlayer pl = ProxyServer.getInstance().getPlayer(args[0]);
        if (pl == null)
        {
            p.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§c" + args[0] + " §cis not online!"));
            return;
        }
        if (!party.isInParty(pl))
        {
            p.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§c" + args[0] + " §cis not in your Party!"));
            return;
        }
        if (party.removePlayer(pl)) {
            pl.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§cYou have been kicked §cfrom the Party!"));
            p.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§6" + P_Player.getRankCol(p) + p.getName() + " §cleft the Party!"));
            for (ProxiedPlayer pp : party.getPlayers()) {
                pp.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§6" + P_Player.getRankCol(pl) + pl.getName() + " §chas been kicked from your Party!"));
            }
            start(p);
        }
    }

    private void start(final ProxiedPlayer p)
    {
        ProxyServer.getInstance().getScheduler().schedule(ABMCSocial.getInstance(), () -> {
            P_Player party = P_Manager.getParty(p);
            if ((party != null) && (party.getPlayers().size() == 0))
            {
                P_Manager.deleteParty(p);
                p.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§cThe party was dissolved because §cof too few members!"));
            }
        }, 2L, TimeUnit.MINUTES);
    }
}