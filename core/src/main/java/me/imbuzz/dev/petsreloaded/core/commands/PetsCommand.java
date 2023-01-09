package me.imbuzz.dev.petsreloaded.core.commands;

import com.google.common.collect.Lists;
import me.imbuzz.dev.petsreloaded.core.PetsReloaded;
import org.bukkit.command.CommandSender;

import java.util.List;

public interface PetsCommand {

    default boolean hasPermission(CommandSender sender) {
        return true;
    }

    String getPermission();

    String getDescription();

    String getUsage();

    boolean onCommand(PetsReloaded petsReloaded, CommandSender sender, String[] args);

    default List<String> tabCompleter(PetsReloaded petsReloaded, CommandSender sender) {
        return Lists.newArrayList();
    }

}
