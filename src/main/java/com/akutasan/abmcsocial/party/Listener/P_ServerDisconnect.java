package com.akutasan.abmcsocial.party.Listener;

import com.akutasan.abmcsocial.ABMCSocial;
import com.akutasan.abmcsocial.party.manager.P_Manager;
import com.akutasan.abmcsocial.party.manager.P_Player;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.concurrent.TimeUnit;

public class P_ServerDisconnect implements Listener {
    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent e) {
        ProxiedPlayer p = e.getPlayer();
        if (P_Manager.getParty(p) != null) {
            P_Player party = P_Manager.getParty(p);
            assert party != null;
            if (party.isLeader(p)) {
                if (party.getPlayers().size() == 1) {
                    for (ProxiedPlayer pp : party.getPlayers()) {
                        pp.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§cAs the Party leader §cleft §cthe §cParty, §cthe §cParty §cwas dissolved."));
                    }
                    party.removePlayer(p);
                    P_Manager.deleteParty(p);
                } else {
                    for (ProxiedPlayer pp : party.getPlayers()) {
                        pp.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§cAs the Party leader §cleft §cthe §cParty, §cthe §cparty §cwas dissolved."));
                        party.removePlayer(p);
                        party.removePlayer(pp);
                    }
                }
            } else {
                party.removePlayer(p);
                for (ProxiedPlayer pp : party.getPlayers()) {
                    pp.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§6" + p.getName() + " §chas §cleft the §cParty §c."));
                }
                start(p);
            }
        }
    }

    private void start(final ProxiedPlayer p)
    {
        ProxyServer.getInstance().getScheduler().schedule(ABMCSocial.getInstance(), () -> {
            P_Player party = P_Manager.getParty(p);
            if ((party != null) && (party.getPlayers().size() == 0)) {
                party.removePlayer(p);
                party.getLeader().sendMessage(new TextComponent(ABMCSocial.partyprefix + "§cThe party was §cdissolved because §cof too few members."));
                p.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§cThe §cParty §cwas §cdissolved."));
            }
        }, 2L, TimeUnit.MINUTES);
    }
}
