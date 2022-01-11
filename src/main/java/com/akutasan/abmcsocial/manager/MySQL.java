package com.akutasan.abmcsocial.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.akutasan.abmcsocial.ABMCSocial;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import org.intellij.lang.annotations.Language;

public class MySQL {
    public String username;
    public String password;
    public String database;
    public String host;
    public Connection con;

    ABMCSocial plugin;

    public MySQL(ABMCSocial plugin) {
        this.plugin = plugin;
    }

    public void connect() {
        if (!isConnected()) {
            try {
                con = DriverManager.getConnection("jdbc:mysql://" + host + ":3306/" + database + "?autoReconnect=true",
                        username, password);
                ProxyServer.getInstance().getConsole()
                        .sendMessage(new TextComponent("§aSuccessfully connected to MySQL-Database."));
            } catch (SQLException e) {
                ProxyServer.getInstance().getConsole()
                        .sendMessage(new TextComponent("§cCould not connect to MySQL-Database, please contact Akutasan!"));
            }
        }
    }

    public void close() {
        if (isConnected()) {
            try {
                con.close();
                con = null;
                ProxyServer.getInstance().getConsole()
                        .sendMessage(new TextComponent("§aSuccessfully closed MySQL-Connection."));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isConnected() {
        return con != null;
    }

    public void update(@Language("SQL")String qry) {
        if (isConnected()) {
            try {
                con.createStatement().executeUpdate(qry);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



    public ResultSet getResult(@Language("SQL")String qry) {
        if (isConnected()) {
            try {
                return con.createStatement().executeQuery(qry);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}