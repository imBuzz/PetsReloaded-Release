package me.imbuzz.dev.petsreloaded.core.utils;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.List;

public class Strings {

    public static String colorize(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> colorize(List<String> message) {
        List<String> returnList = Lists.newArrayList();
        for (String s : message) {
            returnList.add(colorize(s));
        }
        return returnList;
    }

    public static String serializeLocation(Location loc) {
        return loc.getX() + ";" + loc.getY() + ";" + loc.getZ() + ";" + loc.getYaw() + ";" + loc.getPitch() + ";" + loc.getWorld().getName();
    }

    public static Location deserializeLocation(String s) {
        String[] parts = s.split(";");
        return new Location(Bukkit.getWorld(parts[5]),
                Double.parseDouble(parts[0]), Double.parseDouble(parts[1]), Double.parseDouble(parts[2]),
                Float.parseFloat(parts[3]), Float.parseFloat(parts[4]));
    }


}
