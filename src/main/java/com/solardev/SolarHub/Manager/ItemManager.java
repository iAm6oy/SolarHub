package com.solardev.SolarHub.Manager;

import com.solardev.SolarHub.SolarHub;
import com.solardev.SolarHub.utils.chat.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public final class ItemManager implements Manager {

    private SolarHub plugin;

    public static void giveDefaultItems(Player player) {
        player.getInventory().clear();
        //Compass
        ItemStack compass = new ItemStack(Material.COMPASS);
        ItemMeta compassMeta = compass.getItemMeta();
        compassMeta.setDisplayName(Color.translate("&bServer Selector"));
        ArrayList<String> compassLore = new ArrayList<>();
        compassLore.add("");
        compassLore.add(Color.translate("&3Right Click &bto open the Server Selector Menu "));
        compassMeta.setLore(compassLore);
        compass.setItemMeta(compassMeta);
        player.getInventory().addItem(compass);
    }
}
