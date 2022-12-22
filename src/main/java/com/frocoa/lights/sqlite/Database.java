package com.frocoa.lights.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.frocoa.lights.Lights;
import com.frocoa.lights.model.LightBlock;
import com.frocoa.lights.model.LightBlockTemplate;
import com.frocoa.lights.utility.LocationString;
import org.bukkit.Location;
import org.bukkit.entity.Player;



public abstract class Database {
    Lights plugin;
    Connection connection;
    public String table = "lights_placed";
    public Database(Lights instance){
        plugin = instance;
    }

    public abstract Connection getSQLConnection();

    public abstract void load();

    public void initialize(){
        connection = getSQLConnection();
        try{
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + " WHERE player = ?");
            ResultSet rs = ps.executeQuery();
            close(ps,rs);

        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, "Unable to retreive connection", ex);
        }
    }

    // These are the methods you can use to get things out of your database. You of course can make new ones to return different things in the database.
    // This returns the number of people the player killed.
    public List<LightBlock> getLightBlocks() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT template, location FROM " + table + ";");

            rs = ps.executeQuery();

            List<LightBlock> lightBlockList = new ArrayList<>();

            while (rs.next()) {
                String templateName = rs.getString("template");
                String locationString = rs.getString("location");
                Location location = LocationString.parseStringToLocation(locationString);
                LightBlockTemplate template = plugin.getTemplate(templateName);
                LightBlock lightBlock = template.createLightBlock(location);
                lightBlockList.add(lightBlock);
            }

            return lightBlockList;
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
        return new ArrayList<>();
    }

    // Now we need methods to save things to the database
    public void addLightBlockPLaced(LightBlock lightBlock, Player player) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("REPLACE INTO " + table + " (location,player,template) VALUES(?,?,?)"); // IMPORTANT. In SQLite class, We made 3 colums. player, Kills, Total.

            ps.setString(1, LocationString.parseLocationToString(lightBlock.getLocation()));
            ps.setString(2, player.getName().toLowerCase());
            ps.setString(3, lightBlock.getTemplateName().toLowerCase());

            ps.executeUpdate();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
    }

    // Now we need methods to save things to the database
    public void removeLightBlockPlaced(Location location) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            String locationString = LocationString.parseLocationToString(location);
            ps = conn.prepareStatement("DELETE FROM " + table + " WHERE location='" + locationString + "';"); // IMPORTANT. In SQLite class, We made 3 colums. player, Kills, Total.
            ps.executeUpdate();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
    }


    public void close(PreparedStatement ps,ResultSet rs){
        try {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
        } catch (SQLException ex) {
            Error.close(plugin, ex);
        }
    }
}