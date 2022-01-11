package com.akutasan.abmcsocial.party.manager;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class P_Manager extends Plugin {
    private static final List<P_Player> parties = new ArrayList<>();

    public static P_Player getParty(ProxiedPlayer p) {
        for (P_Player party : parties) {
            if (party.isInParty(p)) {
                return party;
            }
        }
        return null;
    }

    public static void createParty(ProxiedPlayer p) {
        if (getParty(p) == null) {
            parties.add(new P_Player(p));
        }
    }

    public static void deleteParty(ProxiedPlayer p) {
        parties.remove(getParty(p));
    }

    public static List<P_Player> getParties()
    {
        return parties;
    }
}