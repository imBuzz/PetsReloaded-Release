package me.imbuzz.dev.petsreloaded.core.commands;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import me.imbuzz.dev.petsreloaded.core.PetsReloaded;
import me.imbuzz.dev.petsreloaded.core.commands.list.PetsBase;
import me.imbuzz.dev.petsreloaded.core.commands.list.impl.*;
import me.imbuzz.dev.petsreloaded.core.utils.Strings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PetsCommands implements CommandExecutor, TabCompleter {

    @Getter
    private final Map<String, PetsCommand> commands = Maps.newHashMap();
    private final PetsReloaded petsReloaded = PetsReloaded.get();

    public PetsCommands() {

        commands.put("pets", new PetsBase());
        commands.put("reload", new PetsReload());
        commands.put("clear", new PetsClearWorld());
        commands.put("clearall", new PetsClear());
        commands.put("sit", new PetsSit());
        commands.put("help", new PetsHelp());
        commands.put("rename", new PetsRename());
        commands.put("spawn", new PetsSpawn());
        commands.put("despawn", new PetsDespawn());

        petsReloaded.getCommand("pets").setExecutor(this);
        petsReloaded.getCommand("pets").setTabCompleter(this);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            if (!commands.containsKey(command.getName().toLowerCase(Locale.ROOT))) return false;

            PetsCommand petsCommand = commands.get(command.getName().toLowerCase(Locale.ROOT));
            if (!petsCommand.hasPermission(sender)) {
                sender.sendMessage(Strings.colorize(petsReloaded.getVariablesContainer().getNoPermissionMessage()));
                return false;
            }
            petsCommand.onCommand(petsReloaded, sender, args);

            return true;
        }
        if (!commands.containsKey(args[0])) return false;

        PetsCommand petsCommand = commands.get(args[0]);
        if (!petsCommand.hasPermission(sender)) {
            sender.sendMessage(petsReloaded.getVariablesContainer().getNoPermissionMessage());
            return false;
        }
        petsCommand.onCommand(petsReloaded, sender, args);

        return false;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!commands.containsKey(args[0])) {
            List<String> strings = Lists.newArrayList();
            commands.forEach((key, value) -> {
                if (value.hasPermission(sender)) strings.add(key);
            });
            return strings;
        }
        PetsCommand petsCommand = commands.get(args[0]);
        return petsCommand.tabCompleter(petsReloaded, sender);
    }
}
