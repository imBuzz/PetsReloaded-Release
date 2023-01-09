package me.imbuzz.dev.petsreloaded.core.commands.list.impl;

import me.imbuzz.dev.petsreloaded.core.PetsReloaded;
import me.imbuzz.dev.petsreloaded.core.commands.PetsCommand;
import me.imbuzz.dev.petsreloaded.core.utils.Strings;
import org.bukkit.command.CommandSender;

public class PetsReload implements PetsCommand {

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission(getPermission());
    }

    @Override
    public String getPermission() {
        return "pets.reload";
    }

    @Override
    public String getDescription() {
        return "Reload the plugin configuration and the pet list";
    }

    @Override
    public String getUsage() {
        return "pets reload";
    }

    @Override
    public boolean onCommand(PetsReloaded petsReloaded, CommandSender sender, String[] args) {
        sender.sendMessage(Strings.colorize(petsReloaded.getVariablesContainer().getReloadingMessage()));
        petsReloaded.reload();
        sender.sendMessage(Strings.colorize(petsReloaded.getVariablesContainer().getReloadMessage()));
        return true;
    }

}
