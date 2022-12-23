package com.frocoa.lights.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.frocoa.lights.Lights;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.logging.Logger;

@CommandAlias("sb|lightblock|scheduledblocks")
public class LightBlockCommand extends BaseCommand {

    private final Lights controller;
    private final Logger logger;

    public LightBlockCommand(Lights controller, Logger logger) {
        this.controller = controller;
        this.logger = logger;
    }

    @HelpCommand
    @CommandPermission("lightblock.use")
    @Description("Shows the help menu")
    public void sendInformation(CommandSender sender) {
        System.out.println("Hola");
    }

    @Subcommand("give")
    @CommandPermission("lightblock.use")
    @CommandCompletion("@lights")
    @Description("gives the player a light")
    public void onGet(CommandSender sender, String[] args) {

        if(!(sender instanceof Player)) {
            logger.warning("This command is meant only for players");
            return;
        }

        Player player = (Player) sender;

        ItemStack itemStack = new ItemStack(Material.REDSTONE_LAMP);
        NamespacedKey key = new NamespacedKey(controller, "lamp-key");

        if (!controller.getTemplates().containsKey(args[0])) {
            player.sendMessage(ChatColor.RED + "The light '" + args[0] +"' doesnt exist");
            return;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, args[0]);
            itemMeta.setDisplayName(ChatColor.BLUE + args[0]);
        }
        itemStack.setItemMeta(itemMeta);
        player.getInventory().addItem(itemStack);
    }
}
