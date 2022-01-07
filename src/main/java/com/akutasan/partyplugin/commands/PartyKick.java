package com.akutasan.partyplugin.commands;

import com.akutasan.partyplugin.Party;
import com.akutasan.partyplugin.manager.PartyManager;
import com.akutasan.partyplugin.manager.PlayerParty;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.concurrent.TimeUnit;

// Kick member from Party
public class PartyKick extends SubCommand {
    public PartyKick()
    {
        super("", "<Player>", "kick");
    }

    public void onCommand(ProxiedPlayer p, String[] args) {
        if (args.length == 0){
            p.sendMessage(new TextComponent(Party.partyprefix + "§cPlease enter a valid §cName!"));
            return;
        }
        if (PartyManager.getParty(p) == null)
        {
            p.sendMessage(new TextComponent(Party.partyprefix + "§cYou are not in §ca Party!"));
            return;
        }
        PlayerParty party = PartyManager.getParty(p);
        assert party != null;
        if (!party.isLeader(p))
        {
            p.sendMessage(new TextComponent(Party.partyprefix + "§cYou are not the §cParty §cleader!"));
            return;
        }
        ProxiedPlayer pl = ProxyServer.getInstance().getPlayer(args[0]);
        if (pl == null)
        {
            p.sendMessage(new TextComponent(Party.partyprefix + "§c" + args[0] + " §cis not online!"));
            return;
        }
        if (!party.isInParty(pl))
        {
            p.sendMessage(new TextComponent(Party.partyprefix + "§c" + args[0] + " §cis not in your Party!"));
            return;
        }
        if (party.removePlayer(pl)) {
            pl.sendMessage(new TextComponent(Party.partyprefix + "§cYou have been kicked §cfrom the Party!"));
            p.sendMessage(new TextComponent(Party.partyprefix + "§6" + PlayerParty.getRankCol(p) + p.getName() + " §cleft the Party!"));
            for (ProxiedPlayer pp : party.getPlayers()) {
                pp.sendMessage(new TextComponent(Party.partyprefix + "§6" + PlayerParty.getRankCol(pl) + pl.getName() + " §chas been kicked from your Party!"));
            }
            start(p);
        }
    }

    private void start(final ProxiedPlayer p)
    {
        ProxyServer.getInstance().getScheduler().schedule(Party.getInstance(), () -> {
            PlayerParty party = PartyManager.getParty(p);
            if ((party != null) && (party.getPlayers().size() == 0))
            {
                PartyManager.deleteParty(p);
                p.sendMessage(new TextComponent(Party.partyprefix + "§cThe party is dissolved because §cof too few members!"));
            }
        }, 2L, TimeUnit.MINUTES);
    }
}