package me.imbuzz.dev.petsreloaded.core.commands.list.impl;

import me.imbuzz.dev.petsreloaded.core.PetsReloaded;
import me.imbuzz.dev.petsreloaded.core.commands.PetsCommand;
import me.imbuzz.dev.petsreloaded.core.utils.Strings;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PetsDespawn implements PetsCommand {

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
        return "Despawn a pet";
    }

    @Override
    public String getUsage() {
        return "pets despawn";
    }

    @Override
    public boolean onCommand(PetsReloaded petsReloaded, CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Strings.colorize(petsReloaded.getVariablesContainer().getCannotExecuteThisCommand()));
            return false;
        }

        Player player = (Player) sender;

        if (!petsReloaded.getPetsManager().hasActivePet(player)) {
            player.sendMessage(Strings.colorize(petsReloaded.getVariablesContainer().getNoPetActive()));
            return false;
        }


        String[] returnM = petsReloaded.getPetsManager().despawnPet(player);
        player.sendMessage(Strings.colorize(petsReloaded.getVariablesContainer().getDespawnPetMessage()
                .replace("%pet%", returnM[1]))
                .replace("%player%", player.getName()));
        return true;
    }

}
