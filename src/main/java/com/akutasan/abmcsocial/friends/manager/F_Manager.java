package com.akutasan.abmcsocial.friends.manager;

import com.akutasan.abmcsocial.ABMCSocial;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class F_Manager {
    ABMCSocial abmcsocial;
    final String Users = "uf_users";
    final String User_Options = "uf_user_options";

    final String requests = "requests";
    final String lastSeen = "lastSeen";
    final String list = "list";

    final String show_msg_join = "show_msg_join";
    final String show_msg_left = "show_msg_left";
    final String show_msg_switch = "show_msg_switch";
    final String allow_requests = "allow_requests";
    final String allow_private_msg = "allow_private_msg";
    final String allow_jump = "allow_jump";
    final String show_broadcast = "show_broadcast";

    public F_Manager(ABMCSocial abmcsocial){
        this.abmcsocial = abmcsocial;
    }

    public void createTables(){
        abmcsocial.getMySQL().update("CREATE TABLE IF NOT EXISTS " + Users + "(Name VARCHAR(16), " +
                "UUID VARCHAR(64), "+lastSeen+" BIGINT, "+requests+" VARCHAR(20000), "+list+" VARCHAR(20000))");
        abmcsocial.getMySQL().update("CREATE TABLE IF NOT EXISTS " + User_Options + "(UUID VARCHAR(64), "+show_msg_join+" BOOL, "+show_msg_left+" BOOL, " +
                ""+show_msg_switch+" BOOL, "+allow_requests+" BOOL, "+allow_private_msg+" BOOL, "+allow_jump+" BOOL, "+show_broadcast+" BOOL)");
    }

    public void registerPlayer(ProxiedPlayer player){
        if (!existPlayer(player.getUniqueId().toString())){
            abmcsocial.getMySQL().update("INSERT INTO "+Users+"(Name, UUID, "+lastSeen+", "+requests+", "+requests+") VALUES" +
                    "('"+player.getName() + "','" + player.getUniqueId().toString() + "','"+System.currentTimeMillis()+"','', '');");
        } else {
            abmcsocial.getMySQL().update("UPDATE "+Users+" SET Name='"+ player.getName()+"' WHERE UUID='"+player.getUniqueId().toString()+ "';");
        }
    }

    /* Friends */

    public String getFriendListRAW(String name){
        return String.valueOf(get(name, "Name", list, Users));
    }

    public List<String> getFriendList(String name){
        String friendList = getFriendListRAW(name);
        List<String> toreturn = new ArrayList<>();
        if (friendList.isEmpty())
            return toreturn;
        String[] friends = friendList.split(";");
        toreturn.addAll(Arrays.asList(friends));
        return toreturn;
    }

    public int getFriendsNumber(String name){
        String friendList = getFriendListRAW(name);
        if (friendList.isEmpty())
            return 0;
        String[] friends = friendList.split(";");
        return friends.length;
    }

    public HashMap<String, List<String>> getList(String name){
        List<String> friendlist = getFriendList(name);
        List<String> fl = new ArrayList<>();

        for (String uuid : friendlist){
            fl.add(getNamebyUUID(uuid));
        }

        List<String> offline = new ArrayList<>();
        List<String> online = new ArrayList<>();

        for (String entry : fl){
            if (ProxyServer.getInstance().getPlayer(entry) != null){
                online.add(entry);
            } else {
                offline.add(entry);
            }
        }

        Collections.sort(offline);
        Collections.sort(online);

        HashMap<String, List<String>> hash = new HashMap<>();
        hash.put("offline", offline);
        hash.put("online", online);

        return hash;
    }

    /* Requests */

    public List<String> getRequestList(String name){
        String requestlist = getRequestListRAW(name);
        List<String> toreturn = new ArrayList<>();
        if (requestlist.isEmpty())
            return toreturn;
        String[] friends = requestlist.split(";");
        toreturn.addAll(Arrays.asList(friends));
        return toreturn;
    }

    public int getRequestsNumber(String name){
        String requestlist = getRequestListRAW(name);
        if (requestlist.isEmpty())
            return 0;
        String[] friends = requestlist.split(";");
        return friends.length;
    }

    public String getRequestListRAW(String name){
        return String.valueOf(get(name, "Name", requests, Users));
    }

    /* Friends Utilities */

    public void add(String whichone, String name, String friend){
        String frlist;
        if (whichone.equalsIgnoreCase("friend")){
            frlist = getFriendListRAW(name);
        } else if (whichone.equalsIgnoreCase("request")){
            frlist = getRequestListRAW(name);
        } else {
            return;
        }
        frlist = frlist + friend + ";";

        abmcsocial.getMySQL().update("UPDATE "+Users+" SET "+requests+"='"+frlist+"' WHERE Name='"+name+"'");
    }

    public void remove(String whichone, String name, String friend){
        String frlist;
        if (whichone.equalsIgnoreCase("friend")){
            frlist = getFriendListRAW(name);
        } else if (whichone.equalsIgnoreCase("request")){
            frlist = getRequestListRAW(name);
        } else {
            return;
        }
        frlist = frlist.replace(friend + ";", "");

        abmcsocial.getMySQL().update("UPDATE "+Users+" SET "+requests+"='"+frlist+"' WHERE Name='"+name+"'");
    }

    /* Settings */

    public boolean existSetting(String uuid){
        try {
            ResultSet rs = abmcsocial.getMySQL().getResult("SELECT * FROM " + User_Options + " WHERE UUID='" + uuid + "';");

            if (rs.next()) {
                return rs.getString("UUID") != null;
            }
            rs.close();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean getSettings(String name, String type) {
        String uuid = getUUIDbyName(name);
        if (existSetting(uuid)) {
            return Boolean.parseBoolean(String.valueOf(get(name, "Name", type, User_Options)));
        } else {
            return true;
        }
    }

    public void setSetting(String name, String type, String value){
        String uuid = getUUIDbyName(name);
        if (!existSetting(uuid)) {
            abmcsocial.getMySQL().update("INSERT INTO " + User_Options + "(UUID, " + show_msg_join + ", " + show_msg_left + ", " + show_msg_switch + ", " +
                    "" + allow_requests + ", " + allow_private_msg + ", " + allow_jump + ", " + show_broadcast + " VALUES" +
                    "('" + uuid + "','true','true','true','true','true','true','true');");
        }
        abmcsocial.getMySQL().update("UPDATE "+User_Options+" SET "+ type + "='" + value + "' WHERE Name='"+name+"';");
    }

    /* General Utility Methods */

    public String getUUIDbyName(String playername) {
        String i = "";
        try {
            ResultSet rs = abmcsocial.getMySQL()
                    .getResult("SELECT * FROM " + Users + " WHERE Name= '" + playername + "'");

            if ((rs.next())) {
                rs.getString("UUID");
            }

            i = rs.getString("UUID");

        } catch (SQLException ignored) {

        }
        return i;
    }

    public String getNamebyUUID(String uuid) {
        String i = "";
        try {
            ResultSet rs = abmcsocial.getMySQL()
                    .getResult("SELECT * FROM " + Users + " WHERE UUID= '" + uuid + "'");

            if ((rs.next())) {
                rs.getString("Name");
            }

            i = rs.getString("Name");

        } catch (SQLException ignored) {

        }
        return i;
    }

    public Object get(String whereresult, String where, String select, String database) {

        ResultSet rs = abmcsocial.getMySQL()
                .getResult("SELECT " + select + " FROM " + database + " WHERE " + where + "='" + whereresult + "'");
        try {
            if(rs.next()) {
                return rs.getObject(select);
            }
        } catch (SQLException e) {
            return "ERROR";
        }

        ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("§cERROR: Get. Please contact a Developer!"));
        return "ERROR";
    }

    public boolean existPlayer(String uuid) {
        try {
            ResultSet rs = abmcsocial.getMySQL().getResult("SELECT * FROM " + Users + " WHERE UUID='" + uuid + "'");

            if (rs.next()) {
                return rs.getString("UUID") != null;
            }
            rs.close();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean existPlayerName(String name) {
        try {
            ResultSet rs = abmcsocial.getMySQL().getResult("SELECT * FROM "+Users+" WHERE Name= '" + name + "'");

            if (rs.next()) {
                return rs.getString("Name") != null;
            }
            rs.close();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
