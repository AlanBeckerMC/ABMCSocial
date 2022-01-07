package com.akutasan.partyplugin.commands;

import com.akutasan.partyplugin.Party;
import com.akutasan.partyplugin.manager.PartyManager;
import com.akutasan.partyplugin.manager.PlayerParty;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PartyAccept extends SubCommand
{
    public PartyAccept()
    {
        super("", "<Player>", "accept");
    }

    public void onCommand(ProxiedPlayer p, String[] args)
    {
        if (args.length == 0)
        {
            p.sendMessage(new TextComponent(Party.partyprefix + "§cPlease enter a valid §cName!"));
            return;
        }
        if (PartyManager.getParty(p) != null)
        {
            p.sendMessage(new TextComponent(Party.partyprefix + "§cYou are already in a Party!."));
            return;
        }
        ProxiedPlayer pl = ProxyServer.getInstance().getPlayer(args[0]);
        if (pl == null)
        {
            p.sendMessage(new TextComponent(Party.partyprefix + "§e" + args[0] + " §cis not online!"));
            return;
        }
        if (PartyManager.getParty(pl) == null)
        {
            p.sendMessage(new TextComponent(Party.partyprefix + "§cThe player " + args[0] + " §cdoesn't have an active Party!"));
            return;
        }
        PlayerParty party = PartyManager.getParty(pl);
        assert party != null;
        if (!party.hasRequest(p)) {
            p.sendMessage(new TextComponent(Party.partyprefix + "§cYou don't have an §cactive invite from §6" + args[0] + " §c!"));
        }
        if (party.isInParty(p))
        {
            p.sendMessage(new TextComponent(Party.partyprefix + "§cYou are already in a Party!"));
            return;
        }
        if (party.addPlayer(p))
        {
            for (ProxiedPlayer pp : party.getPlayers()) {
                pp.sendMessage(new TextComponent(Party.partyprefix + "§6" + PlayerParty.getRankCol(p) + p + " §ejoined the Party§e!"));
            }
            party.getLeader().sendMessage(new TextComponent(Party.partyprefix + "§6" + PlayerParty.getRankCol(p) + p + " §ejoined the Party!"));
            party.removeInvite(p);
        }
    }
}

