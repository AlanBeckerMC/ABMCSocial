package com.akutasan.abmcsocial.party.Listener;

import com.akutasan.abmcsocial.ABMCSocial;
import com.akutasan.abmcsocial.party.manager.P_Manager;
import com.akutasan.abmcsocial.party.manager.P_Player;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class P_ServerSwitch
        implements Listener
{
    @EventHandler
    public void onSwitch(ServerSwitchEvent e)
    {
        ProxiedPlayer p = e.getPlayer();
        final P_Player party;
        if ((P_Manager.getParty(p) != null) && ((Objects.requireNonNull(party = P_Manager.getParty(p))).isLeader(p)))
        {
            if ((party.getLeader().getServer().getInfo().getName().contains("Lobby")) || (party.getLeader().getServer().getInfo().getName().contains("Lobby01")) || (party.getLeader().getServer().getInfo().getName().contains("Lobby-01"))) {
                return;
            }
            party.getLeader().sendMessage(new TextComponent(ABMCSocial.partyprefix + "§eThe Party is joining the server §6" + party.getLeader().getServer().getInfo().getName()));
            for (final ProxiedPlayer pp : party.getPlayers())
            {
                pp.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§eThe Party is joining the server §6" + party.getLeader().getServer().getInfo().getName()));
                ProxyServer.getInstance().getScheduler().schedule(ABMCSocial.getInstance(), () -> pp.connect(party.getServer()), 2L, TimeUnit.SECONDS);
            }
        }
    }


    @EventHandler
    public void onMessage(PluginMessageEvent event) {
        if (event.getTag().equalsIgnoreCase("BungeeCord")) {

            DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));

            try {
                String channel = in.readUTF();
                if (channel.equalsIgnoreCase("party")) {
                    String input = in.readUTF();
                    String target = in.readUTF();
                    ProxiedPlayer player = ProxyServer.getInstance().getPlayer(event.getReceiver().toString());
                    if (input.equalsIgnoreCase("invite")){
                        ProxiedPlayer pl = ProxyServer.getInstance().getPlayer(target);
                        P_Player party;
                        if ((P_Manager.getParty(player) != null) && (!Objects.requireNonNull(P_Manager.getParty(player)).isLeader(player))) {
                            player.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§cYou are not the party §cleader."));
                        }
                        if ((P_Manager.getParty(player) != null) && ((Objects.requireNonNull(P_Manager.getParty(player))).hasRequest(pl)))
                        {
                            player.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§cThe §cplayer §chat already received an §cinvitation §cto §cyour §cparty."));
                            return;
                        }
                        if (P_Manager.getParty(pl) != null)
                        {
                            player.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§cThe player is already in §cean §cother §cParty."));
                            return;
                        }
                        if (P_Manager.getParty(player) != null)
                        {
                            party = P_Manager.getParty(player);
                            assert party != null;
                            if (party.isInParty(pl)) {
                                player.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§cThe player is already §cin §cyour §cparty."));
                                return;
                            }
                            player.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§aYou have invited §6" + target + " §ainto your §aParty §ae."));
                            party.invite(pl);
                        }
                        else
                        {
                            P_Manager.createParty(player);
                            player.sendMessage(new TextComponent(ABMCSocial.partyprefix + "§aYou invited §6" + target + " §ainto your party §aeinvited."));
                            party = P_Manager.getParty(player);
                            assert party != null;
                            party.invite(pl);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}