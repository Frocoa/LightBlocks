package com.frocoa.lights;

import co.aikar.commands.BukkitCommandManager;
import com.frocoa.lights.commands.LightBlockCommand;
import com.frocoa.lights.listeners.PlaceLight;
import com.frocoa.lights.listeners.RemoveLight;
import com.frocoa.lights.model.LightBlock;
import com.frocoa.lights.model.LightBlockTemplate;
import com.frocoa.lights.sqlite.Database;
import com.frocoa.lights.sqlite.SQLite;
import com.frocoa.lights.utility.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Lightable;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.logging.Logger;

public final class Lights extends JavaPlugin {

    private List<LightBlock> lightBlocks = new ArrayList<>();
    private final Hashtable<String , LightBlockTemplate> templates = new Hashtable<>();
    private BukkitCommandManager manager;
    private Database db;

    @Override
    public void onEnable() {
        Logger logger = Bukkit.getLogger();
        PluginDescriptionFile pluginDescriptionFile = this.getDescription();
        logger.info(pluginDescriptionFile.getName() + " Version: " +
                pluginDescriptionFile.getVersion() + " enabled!");

        // Plugin startup logic
        saveDefaultConfig();
        createLightTemplates();
        Bukkit.getScheduler().runTaskTimer(this, this::execute, 100, 100);

        // commands
        manager = new BukkitCommandManager(this);
        registerCommands();

        // events
        registerEvents();

        // database
        db = new SQLite(this);
        db.load();
        this.lightBlocks = db.getLightBlocks();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerCommands() {
        manager.registerCommand(new LightBlockCommand(this, getLogger()));
    }

    private void registerEvents() {
        // It's better to have listeners in a separate class, for organization.
        Bukkit.getPluginManager().registerEvents(new PlaceLight(this), this);
        Bukkit.getPluginManager().registerEvents(new RemoveLight(this), this);
    }

    private void execute() {
        long time = Bukkit.getWorlds().get(0).getTime();
        for (LightBlock lightBlock : lightBlocks) {
            Location location = lightBlock.getLocation();
            Material material = lightBlock.getCurrentMaterial(time);
            Block block = location.getBlock();
            block.setType(material);

            if (lightBlock.getCurrentLit(time)) {
                BlockData data = block.getBlockData();
                if (data instanceof Lightable) {
                    ((Lightable) data).setLit(true);
                    block.setBlockData(data);
                }
            }
        }
    }

    public void addLightBlock (LightBlock lightBlock) {
        this.lightBlocks.add(lightBlock);
    }

    public boolean locationInList(Location location) {
        for (LightBlock lightBlock : lightBlocks) {
            if (lightBlock.getLocation().equals(location)) return true;
        }
        return false;
    }

    public void removeLight(Location location) {
        lightBlocks.removeIf(lightBlock -> lightBlock.getLocation().equals(location));
    }

    public void createLightTemplates() {
        Set<String> keys = ConfigManager.getLightBlocks(getConfig()).getKeys(false);

        // each loop is one LightBlock
        for (String key: keys) {
            List<Long> hours = getConfig().getLongList("LightBlocks." + key + ".hours");
            List<String> materials = getConfig().getStringList("LightBlocks." + key + ".materials");

            // each loop is one scheduled material
            LightBlockTemplate template = new LightBlockTemplate(key);
            for (int i = 0 ; i < hours.size() ; i++) {
                String material = materials.get(i);
                String[] array = material.split(" ");
                boolean lit = false;
                if (array.length >= 2) {
                    if (array[1].equalsIgnoreCase("lit")) lit = true;
                }

                template.addSchedule(hours.get(i), Material.valueOf(array[0]), lit);
            }
            templates.put(key, template);
        }
    }

    public Hashtable<String, LightBlockTemplate> getTemplates() {
        return templates;
    }

    public LightBlockTemplate getTemplate(String name) {
        return templates.get(name);
    }

    public Database getDb() {
        return db;
    }
}
