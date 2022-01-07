package com.akutasan.partyplugin.Listener;

import com.akutasan.partyplugin.Party;
import com.akutasan.partyplugin.manager.PartyManager;
import com.akutasan.partyplugin.manager.PlayerParty;
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

public class ServerSwitch
        implements Listener
{
    @EventHandler
    public void onSwitch(ServerSwitchEvent e)
    {
        ProxiedPlayer p = e.getPlayer();
        final PlayerParty party;
        if ((PartyManager.getParty(p) != null) && ((Objects.requireNonNull(party = PartyManager.getParty(p))).isLeader(p)))
        {
            if ((party.getLeader().getServer().getInfo().getName().contains("Lobby")) || (party.getLeader().getServer().getInfo().getName().contains("Lobby01")) || (party.getLeader().getServer().getInfo().getName().contains("Lobby-01"))) {
                return;
            }
            party.getLeader().sendMessage(new TextComponent(Party.partyprefix + "§eThe Party is joining the server §6" + party.getLeader().getServer().getInfo().getName()));
            for (final ProxiedPlayer pp : party.getPlayers())
            {
                pp.sendMessage(new TextComponent(Party.partyprefix + "§eThe Party is joining the server §6" + party.getLeader().getServer().getInfo().getName()));
                ProxyServer.getInstance().getScheduler().schedule(Party.getInstance(), new Runnable()
                {
                    public void run()
                    {
                        pp.connect(party.getServer());
                    }
                }, 2L, TimeUnit.SECONDS);
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
                        PlayerParty party;
                        if ((PartyManager.getParty(player) != null) && (!Objects.requireNonNull(PartyManager.getParty(player)).isLeader(player))) {
                            player.sendMessage(new TextComponent(Party.partyprefix + "§cYou are not the party §cleader."));
                        }
                        if ((PartyManager.getParty(player) != null) && ((Objects.requireNonNull(PartyManager.getParty(player))).hasRequest(pl)))
                        {
                            player.sendMessage(new TextComponent(Party.partyprefix + "§cThe §cplayer §chat already received an §cinvitation §cto §cyour §cparty."));
                            return;
                        }
                        if (PartyManager.getParty(pl) != null)
                        {
                            player.sendMessage(new TextComponent(Party.partyprefix + "§cThe player is already in §cean §cother §cParty."));
                            return;
                        }
                        if (PartyManager.getParty(player) != null)
                        {
                            party = PartyManager.getParty(player);
                            assert party != null;
                            if (party.isInParty(pl)) {
                                player.sendMessage(new TextComponent(Party.partyprefix + "§cThe player is already §cin §cyour §cparty."));
                                return;
                            }
                            player.sendMessage(new TextComponent(Party.partyprefix + "§aYou have invited §6" + target + " §ainto your §aParty §ae."));
                            party.invite(pl);
                        }
                        else
                        {
                            PartyManager.createParty(player);
                            player.sendMessage(new TextComponent(Party.partyprefix + "§aYou invited §6" + target + " §ainto your party §aeinvited."));
                            party = PartyManager.getParty(player);
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