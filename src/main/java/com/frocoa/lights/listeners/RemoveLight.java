package com.frocoa.lights.listeners;

import com.frocoa.lights.Lights;
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
        if (!plugin.locationInList(Objects.requireNonNull(block).getLocation())) return;

        block.setType(Material.AIR);
        plugin.removeLight(block.getLocation());

        Player player = event.getPlayer();
        player.sendMessage("Removing light...");
    }
}
