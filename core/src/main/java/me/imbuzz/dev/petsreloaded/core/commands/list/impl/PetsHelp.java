package me.imbuzz.dev.petsreloaded.core.commands.list.impl;

import me.imbuzz.dev.petsreloaded.core.PetsReloaded;
import me.imbuzz.dev.petsreloaded.core.commands.PetsCommand;
import me.imbuzz.dev.petsreloaded.core.utils.Strings;
import org.bukkit.command.CommandSender;

public class PetsHelp implements PetsCommand {

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission(getPermission());
    }

    @Override
    public String getPermission() {
        return "pets.help";
    }

    @Override
    public String getDescription() {
        return "Open the commands list";
    }

    @Override
    public String getUsage() {
        return "pets help";
    }

    @Override
    public boolean onCommand(PetsReloaded petsReloaded, CommandSender sender, String[] args) {
        sender.sendMessage("");
        sender.sendMessage(Strings.colorize("  &7Running &a&lPetsReloaded " + petsReloaded.getDescription().getVersion() + " &7by &aImBuzz"));
        sender.sendMessage("");
        for (PetsCommand value : petsReloaded.getPetsCommands().getCommands().values()) {
            sender.sendMessage(Strings.colorize(" &a/" + value.getUsage() + " - " + value.getPermission() + " - &7" + value.getDescription()));
        }
        sender.sendMessage("");

        return true;
    }

}
