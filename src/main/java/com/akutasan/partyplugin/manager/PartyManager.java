package com.akutasan.partyplugin.manager;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class PartyManager extends Plugin {
    private static final List<PlayerParty> parties = new ArrayList<>();

    public static PlayerParty getParty(ProxiedPlayer p) {
        for (PlayerParty party : parties) {
            if (party.isInParty(p)) {
                return party;
            }
        }
        return null;
    }

    public static void createParty(ProxiedPlayer p) {
        if (getParty(p) == null) {
            parties.add(new PlayerParty(p));
        }
    }

    public static void deleteParty(ProxiedPlayer p) {
        parties.remove(getParty(p));
    }

    public static List<PlayerParty> getPartys()
    {
        return parties;
    }
}