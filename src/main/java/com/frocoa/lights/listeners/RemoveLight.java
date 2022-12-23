package com.frocoa.lights.listeners;

import com.frocoa.lights.Lights;
import com.frocoa.lights.model.LightBlock;
import com.frocoa.lights.sqlite.Database;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;


import java.util.Objects;

public class RemoveLight implements Listener {

    Lights plugin;

    public RemoveLight(Lights plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRemoveLight(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.LEFT_CLICK_BLOCK)) return;

        Block block = event.getClickedBlock();
        Location location = Objects.requireNonNull(block).getLocation();
        if (!plugin.locationInList(location)) return;
        Player player = event.getPlayer();
        LightBlock lightBlock = plugin.getLightBlock(location);
        if (!player.getName().equals(lightBlock.getPlayerName()) && !player.hasPermission("lightblock.break")) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "THIS IS NOT YOUR LIGHT!");
            return;
        }

        block.setType(Material.AIR, false);
        plugin.removeLight(location);

        player.sendMessage("Removing light...");

        Database db = plugin.getDb();
        db.removeLightBlockPlaced(location);
    }
}
