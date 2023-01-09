package me.imbuzz.dev.petsreloaded.core.utils;

import com.google.common.collect.Lists;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class FileUtils {

    public static String getString(ConfigurationSection section, String tag){
        return section.getString(tag) == null ? "" : section.getString(tag);
    }

    public static List<String> getStringList(ConfigurationSection file, String tag){
        return file.getStringList(tag) == null ? Lists.newArrayList() : file.getStringList(tag);
    }

    public static String getString(FileConfiguration file, String tag){
        return file.getString(tag) == null ? "" : file.getString(tag);
    }

    public static List<String> getStringList(FileConfiguration file, String tag){
        return file.getStringList(tag) == null ? Lists.newArrayList() : file.getStringList(tag);
    }



}
