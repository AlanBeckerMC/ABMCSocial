package com.akutasan.partyplugin.commands;


import com.akutasan.partyplugin.Party;
import com.akutasan.partyplugin.manager.PartyManager;
import com.akutasan.partyplugin.manager.PlayerParty;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Objects;

public class PartyInvite extends SubCommand {
    public PartyInvite()
    {
        super("", "<Player>", "invite");
    }

    public void onCommand(ProxiedPlayer p, String[] args) {
        if (args.length == 0) {
            p.sendMessage(new TextComponent(Party.partyprefix + "§cPlease enter a valid §cName!"));
            return;
        }
        ProxiedPlayer pl = ProxyServer.getInstance().getPlayer(args[0]);
        if (pl == null)
        {
            p.sendMessage(new TextComponent(Party.partyprefix + "§e" + args[0] + " §cis not online!"));
            return;
        }
        else if (pl.getName().equals(p.getName()))
        {
            p.sendMessage(new TextComponent(Party.partyprefix + "§cYou cannot invite yourself!"));
            return;
        }

        PlayerParty party;
        if ((PartyManager.getParty(p) != null) && (!Objects.requireNonNull(PartyManager.getParty(p)).isLeader(p))) {
            p.sendMessage(new TextComponent(Party.partyprefix + "§cYou are not the §cParty leader!"));
            return;
        } else if ((PartyManager.getParty(p) != null) && (Objects.requireNonNull(PartyManager.getParty(p)).hasRequest(pl))){
            p.sendMessage(new TextComponent(Party.partyprefix + "§cThe player has already been invited to your Party!"));
            return;
        } else if (PartyManager.getParty(pl) != null) {
            p.sendMessage(new TextComponent(Party.partyprefix + "§cThe player is §calready in another Party!"));
            return;
        }

        if (PartyManager.getParty(p) != null)
        {
            party = PartyManager.getParty(p);
            assert party != null;
            if (party.isInParty(pl)) {
                p.sendMessage(new TextComponent(Party.partyprefix + "§cThe player is §calready in your Party!"));
                return;
            }
            p.sendMessage(new TextComponent(Party.partyprefix + "§aYou have invited §6" + PlayerParty.getRankCol(pl) + pl + " §ainto your Party!"));
            party.invite(pl);
        } else {
            PartyManager.createParty(p);
            p.sendMessage(new TextComponent(Party.partyprefix + "§aYou have invited §6" + PlayerParty.getRankCol(pl) + pl + " §ainto your Party!"));
            party = PartyManager.getParty(p);
            assert party != null;
            party.invite(pl);
        }
    }
}