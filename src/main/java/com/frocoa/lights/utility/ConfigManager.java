package com.frocoa.lights.utility;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;

public class ConfigManager {

    public static ConfigurationSection getLightBlocks(FileConfiguration config) {
        return (Objects.requireNonNull(
                config.getConfigurationSection("LightBlocks"),
                "LightBlocks Section not found in the config")
        );
    }
}
