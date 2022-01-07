package com.akutasan.partyplugin.Listener;

import com.akutasan.partyplugin.Party;
import com.akutasan.partyplugin.manager.PartyManager;
import com.akutasan.partyplugin.manager.PlayerParty;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.concurrent.TimeUnit;

public class ServerDisconnect implements Listener {
    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent e) {
        ProxiedPlayer p = e.getPlayer();
        if (PartyManager.getParty(p) != null) {
            PlayerParty party = PartyManager.getParty(p);
            assert party != null;
            if (party.isLeader(p)) {
                if (party.getPlayers().size() == 1) {
                    for (ProxiedPlayer pp : party.getPlayers()) {
                        pp.sendMessage(new TextComponent(Party.partyprefix + "§cSince the Party leader §cleft §cthe §cParty, §cthe §cParty §cwas dissolved."));
                    }
                    party.removePlayer(p);
                    PartyManager.deleteParty(p);
                } else {
                    for (ProxiedPlayer pp : party.getPlayers()) {
                        pp.sendMessage(new TextComponent(Party.partyprefix + "§cSince the Party leader §cleft §cthe §cParty, §cthe §cparty §cwas dissolved."));
                        party.removePlayer(p);
                        party.removePlayer(pp);
                    }
                }
            } else {
                party.removePlayer(p);
                for (ProxiedPlayer pp : party.getPlayers()) {
                    pp.sendMessage(new TextComponent(Party.partyprefix + "§6" + p.getName() + " §chas §cleft the §cParty §c."));
                }
                start(p);
            }
        }
    }

    private void start(final ProxiedPlayer p)
    {
        ProxyServer.getInstance().getScheduler().schedule(Party.getInstance(), () -> {
            PlayerParty party = PartyManager.getParty(p);
            if ((party != null) && (party.getPlayers().size() == 0)) {
                party.removePlayer(p);
                party.getLeader().sendMessage(new TextComponent(Party.partyprefix + "§cThe party is §cdissolved because §cof too few members."));
                p.sendMessage(new TextComponent(Party.partyprefix + "§cThe §cParty §cwas §cdissolved."));
            }
        }, 2L, TimeUnit.MINUTES);
    }
}
