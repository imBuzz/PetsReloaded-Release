package me.imbuzz.dev.petsreloaded.core.utils;

import org.bukkit.Color;
import org.bukkit.util.EulerAngle;

public class ItemTools {

    public static Color getColorFromString(String string){
        String[] split = string.split(":");
        return Color.fromRGB(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
    }

    public static EulerAngle getAngleFromString(String string){
        if (string == null || string.isEmpty()) return new EulerAngle(0, 0, 0);
        String[] split = string.split(":");
        return new EulerAngle(Math.toRadians(Integer.parseInt(split[0])),
                Math.toRadians(Integer.parseInt(split[1])),
                Math.toRadians(Integer.parseInt(split[2])));
    }



}
