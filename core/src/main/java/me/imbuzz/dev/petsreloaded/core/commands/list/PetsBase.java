package me.imbuzz.dev.petsreloaded.core.commands.list;

import com.google.common.collect.Lists;
import me.imbuzz.dev.petsreloaded.core.PetsReloaded;
import me.imbuzz.dev.petsreloaded.core.commands.PetsCommand;
import me.imbuzz.dev.petsreloaded.core.gui.PetsGUI;
import me.imbuzz.dev.petsreloaded.core.utils.Strings;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class PetsBase implements PetsCommand {

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission(getPermission());
    }

    @Override
    public String getPermission() {
        return "pets.base";
    }

    @Override
    public String getDescription() {
        return "Open the basic GUI where you can see every locked/unlocked pet";
    }

    @Override
    public String getUsage() {
        return "pets";
    }

    @Override
    public boolean onCommand(PetsReloaded petsReloaded, CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Strings.colorize(petsReloaded.getVariablesContainer().getCannotExecuteThisCommand()));
            return false;
        }

        PetsGUI.getInventory(petsReloaded).open(((Player) sender));
        return true;
    }

    @Override
    public List<String> tabCompleter(PetsReloaded petsReloaded, CommandSender sender) {
        List<String> types = Lists.newArrayList();

        petsReloaded.getPetsCommands().getCommands().forEach((key, value) -> {
            if (!key.equalsIgnoreCase("pets")) {
                if (value.hasPermission(sender)) {
                    types.add(key);
                }
            }
        });
        return types;
    }
}
