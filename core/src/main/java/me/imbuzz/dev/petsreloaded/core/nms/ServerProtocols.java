package me.imbuzz.dev.petsreloaded.core.nms;

import me.imbuzz.dev.petsreloaded.core.PetsReloaded;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ServerProtocols {

    public static String getServerVersion() {
         String name = Bukkit.getServer().getClass().getPackage().getName();
        String version = name.substring(name.lastIndexOf('.') + 1);

        if (version.contains("v1_19")){
            String bukkitVersion = Bukkit.getBukkitVersion();
            String[] array = bukkitVersion.split("-");
            return "v" +  (array[0] + "_" + array[1]).replace(".", "_");
        }

        if (version.contains("v1_17") && !Bukkit.getServer().getVersion().matches("(.*)1\\.17\\.\\d(.*)"))
            version = "v1_17_R0";
        if (version.contains("v1_18") && !Bukkit.getServer().getVersion().matches("(.*)1\\.18\\.\\d(.*)"))
            version = "v1_18_R0";
        return version;
    }

    public static INMSHandler getNmsHandler(JavaPlugin javaPlugin) {
        try {
            return (INMSHandler) Class.forName("me.imbuzz.dev.petsreloaded.nms." + getServerVersion() + ".NMSHandler").getConstructor(PetsReloaded.class).newInstance(javaPlugin);
        } catch (Exception exc) {
            exc.printStackTrace();
            javaPlugin.getLogger().severe("You are running PetsReloaded on an unsupported NMS version " + getServerVersion() + " stopping...");
            return null;
        }
    }

}
