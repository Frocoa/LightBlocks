package com.frocoa.lights.listeners;

import com.frocoa.lights.model.LightBlock;
import com.frocoa.lights.Lights;
import com.frocoa.lights.model.LightBlockTemplate;
import com.frocoa.lights.sqlite.Database;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Hashtable;

public class PlaceLight implements Listener {

    Lights plugin;

    public PlaceLight(Lights plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPLayerPLaceLight(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItemInHand();
        NamespacedKey key = new NamespacedKey(plugin, "lamp-key");
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            PersistentDataContainer container = itemMeta.getPersistentDataContainer();

            if (container.has(key, PersistentDataType.STRING)) {
                String template_key = container.get(key, PersistentDataType.STRING);
                Hashtable<String, LightBlockTemplate> templates = plugin.getTemplates();

                if (!templates.containsKey(template_key)) {
                    player.sendMessage(ChatColor.RED + "THIS LAMP DOESNT EXIST");
                    return;
                }

                LightBlockTemplate template = templates.get(template_key);
                LightBlock my_block = template.createLightBlock(event.getBlock().getLocation());
                plugin.addLightBlock(my_block);
                player.sendMessage("PLacing light...");
                event.setCancelled(true);

                Database db = plugin.getDb();
                db.addLightBlockPLaced(my_block, player);
            }
        }
    }
}
