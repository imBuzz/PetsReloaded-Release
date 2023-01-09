package me.imbuzz.dev.petsreloaded.core.commands.list.impl;

import me.imbuzz.dev.petsreloaded.core.PetsReloaded;
import me.imbuzz.dev.petsreloaded.core.commands.PetsCommand;
import me.imbuzz.dev.petsreloaded.core.objects.pets.PetEntity;
import me.imbuzz.dev.petsreloaded.core.utils.Strings;
import org.bukkit.command.CommandSender;

public class PetsClear implements PetsCommand {

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission(getPermission());
    }

    @Override
    public String getPermission() {
        return "pets.clear";
    }

    @Override
    public String getDescription() {
        return "Remove every spawned pet on your server";
    }

    @Override
    public String getUsage() {
        return "pets clearall";
    }

    @Override
    public boolean onCommand(PetsReloaded petsReloaded, CommandSender sender, String[] args) {
        for (PetEntity value : petsReloaded.getPetsManager().getActivePets()) {
            petsReloaded.getPetsManager().despawnPet(value);
        }

        sender.sendMessage(Strings.colorize(petsReloaded
                .getVariablesContainer().getClearGlobal()));
        return true;
    }

}
