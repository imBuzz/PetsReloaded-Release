package me.imbuzz.dev.petsreloaded.core.commands.list.impl;

import me.imbuzz.dev.petsreloaded.core.PetsReloaded;
import me.imbuzz.dev.petsreloaded.core.commands.PetsCommand;
import me.imbuzz.dev.petsreloaded.core.objects.pets.PetEntity;
import me.imbuzz.dev.petsreloaded.core.objects.player.PetsPlayer;
import me.imbuzz.dev.petsreloaded.core.utils.Strings;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PetsRename implements PetsCommand {

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission(getPermission());
    }

    @Override
    public String getPermission() {
        return "pets.rename";
    }

    @Override
    public String getDescription() {
        return "Rename your current active pet";
    }

    @Override
    public String getUsage() {
        return "pets rename <name>";
    }

    @Override
    public boolean onCommand(PetsReloaded petsReloaded, CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Strings.colorize(
                    petsReloaded.getVariablesContainer().getCannotExecuteThisCommand()));
            return false;
        }

        if (args.length != 2) {
            sender.sendMessage(Strings.colorize(petsReloaded.getVariablesContainer().getCommandError()));
            return false;
        }

        Player player = ((Player) sender);

        if (!petsReloaded.getPetsManager().hasActivePet(player)) {
            sender.sendMessage(Strings.colorize(petsReloaded.getVariablesContainer().getNeedActivePet()));
            return false;
        }

        String newName = args[1];
        for (String inappropriateWord : petsReloaded.getVariablesContainer().getBlacklistedWordsFromRename()) {
            if (newName.toLowerCase().contains(inappropriateWord.toLowerCase())) {
                sender.sendMessage(Strings.colorize(petsReloaded.getVariablesContainer().getInappropriateWord()));
                return false;
            }
        }

        newName = Strings.colorize(newName);
        if (ChatColor.stripColor(newName).length() > petsReloaded.getVariablesContainer().getMaxCharsRename()) {
            sender.sendMessage(Strings.colorize(petsReloaded.getVariablesContainer().getMaxLengthForRename().replace("{max}",
                    String.valueOf(petsReloaded.getVariablesContainer().getMaxCharsRename()))));
            return false;
        }


        PetEntity petEntity = (PetEntity) petsReloaded.getPetsManager().getPet(player);

        PetsPlayer petsPlayer = petsReloaded.getPetsManager().getPlayers().get(player.getName());
        petsPlayer.getPetNames().put(petEntity.getSettings().getTag(), newName);

        Location location = petEntity.getHeadEntity().getLocation();

        petsReloaded.getPetsManager().despawnPet(player);
        petsReloaded.getPetsManager().spawnPet(petEntity.getSettings().getTag(), player, location);

        sender.sendMessage(Strings.colorize(petsReloaded
                .getVariablesContainer().getPetRename()));

        return true;
    }

}
