package me.imbuzz.dev.petsreloaded.core.commands.list.impl;

import me.imbuzz.dev.petsreloaded.core.PetsReloaded;
import me.imbuzz.dev.petsreloaded.core.commands.PetsCommand;
import me.imbuzz.dev.petsreloaded.core.objects.pets.PetEntity;
import me.imbuzz.dev.petsreloaded.core.utils.Strings;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PetsClearWorld implements PetsCommand {


    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission(getPermission());
    }

    @Override
    public String getPermission() {
        return "pets.clearworld";
    }

    @Override
    public String getDescription() {
        return "Remove every spawned pet on the written world";
    }

    @Override
    public String getUsage() {
        return "pets clear <world>";
    }

    @Override
    public boolean onCommand(PetsReloaded petsReloaded, CommandSender sender, String[] args) {
        World world;

        if (!(sender instanceof Player)) {
            if (args.length != 2) {
                sender.sendMessage(Strings.colorize(petsReloaded.getVariablesContainer().getCommandError()));
                return false;
            }
            world = Bukkit.getWorld(args[1]);
        } else {
            if (args.length != 1) {
                world = Bukkit.getWorld(args[1]);
            } else world = ((Player) sender).getWorld();
        }

        if (world == null) {
            sender.sendMessage(Strings.colorize(petsReloaded.getVariablesContainer().getCommandError()));
            return false;
        }


        for (PetEntity value : petsReloaded.getPetsManager().getActivePets()) {
            if (!value.getHeadEntity().getWorld().equals(world)) continue;
            petsReloaded.getPetsManager().despawnPet(value);
        }

        sender.sendMessage(Strings.colorize(petsReloaded
                .getVariablesContainer().getClearWorldCompleted().replace("%world%", world.getName())));
        return true;
    }

}
