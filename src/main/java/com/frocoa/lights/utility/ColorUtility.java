package com.frocoa.lights.utility;

import org.bukkit.ChatColor;

public class ColorUtility {

    public static String colorFormat(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
