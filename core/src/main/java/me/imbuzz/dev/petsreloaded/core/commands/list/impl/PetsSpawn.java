package me.imbuzz.dev.petsreloaded.core.commands.list.impl;

import me.imbuzz.dev.petsreloaded.core.PetsReloaded;
import me.imbuzz.dev.petsreloaded.core.commands.PetsCommand;
import me.imbuzz.dev.petsreloaded.core.objects.pets.settings.EntitySettings;
import me.imbuzz.dev.petsreloaded.core.utils.Strings;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PetsSpawn implements PetsCommand {

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
        return "Spawn a pet";
    }

    @Override
    public String getUsage() {
        return "pets spawn <name>";
    }

    @Override
    public boolean onCommand(PetsReloaded petsReloaded, CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Strings.colorize(petsReloaded.getVariablesContainer().getCannotExecuteThisCommand()));
            return false;
        }

        Player player = (Player) sender;

        if (args.length != 2) {
            player.sendMessage(Strings.colorize(petsReloaded.getVariablesContainer().getCommandError()));
            return false;
        }

        if (!petsReloaded.getPetsManager().getPetsPresets().containsKey(args[1])) {
            player.sendMessage(Strings.colorize(petsReloaded.getVariablesContainer().getInvalidPet()
                    .replace("%pet%", args[1])));
            return false;
        }

        EntitySettings entitySettings = petsReloaded.getPetsManager().getPetsPresets().get(args[1]);
        boolean unlocked = player.hasPermission(entitySettings.getPermission()) || player.hasPermission("petsreloaded.pets.*");

        if (unlocked) {
            petsReloaded.getPetsManager().spawnPet(args[1], player);
            player.sendMessage(Strings.colorize(petsReloaded.getVariablesContainer().getSpawnPetMessage()
                            .replace("%pet%", entitySettings.getPetName()))
                    .replace("%player%", player.getName()));
        } else {
            player.sendMessage(Strings.colorize(petsReloaded.getVariablesContainer().getBuyPetMessage()));
        }

        return true;
    }

}
