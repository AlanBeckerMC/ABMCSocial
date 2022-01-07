package com.akutasan.partyplugin.commands;

import com.akutasan.partyplugin.Party;
import com.akutasan.partyplugin.manager.PartyManager;
import com.akutasan.partyplugin.manager.PlayerParty;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PartyDeny extends SubCommand
{
    public PartyDeny()
    {
        super("", "<Player>", "deny");
    }

    public void onCommand(ProxiedPlayer p, String[] args)
    {
        if (args.length == 0)
        {
            p.sendMessage(new TextComponent(Party.partyprefix + "§cPlease enter a valid Name!"));
            return;
        }
        if (PartyManager.getParty(p) != null)
        {
            p.sendMessage(new TextComponent(Party.partyprefix + "§cYou are already §cin a Party!"));
            return;
        }
        ProxiedPlayer pl = ProxyServer.getInstance().getPlayer(args[0]);
        if (pl == null)
        {
            p.sendMessage(new TextComponent(Party.partyprefix + "§cThis player is not online!"));
            return;
        }
        if (PartyManager.getParty(pl) == null)
        {
            p.sendMessage(new TextComponent(Party.partyprefix + "§cThe player §e" + args[0] + " §cdoes not have a Party!"));
            return;
        }
        PlayerParty party = PartyManager.getParty(pl);
        assert party != null;
        if (!party.hasRequest(p))
        {
            p.sendMessage(new TextComponent(Party.partyprefix + "§cYou are not invited to this party!"));
            return;
        }
        party.removeInvite(p);
        p.sendMessage(new TextComponent(Party.partyprefix + "§cYou declined the invite of §e" + PlayerParty.getRankCol(party.getLeader()) + party.getLeader() + " §c!"));
        pl.sendMessage(new TextComponent(Party.partyprefix + "§e" + PlayerParty.getRankCol(p) + p.getName() + " §cdeclined your invite!"));
        if (party.getPlayers().size() == 1) {
            party.removePlayer(pl);
        }
    }
}
