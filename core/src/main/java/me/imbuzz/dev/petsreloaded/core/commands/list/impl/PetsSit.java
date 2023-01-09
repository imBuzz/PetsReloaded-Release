package me.imbuzz.dev.petsreloaded.core.commands.list.impl;

import me.imbuzz.dev.petsreloaded.api.events.PetChangeSitEvent;
import me.imbuzz.dev.petsreloaded.api.objects.IPetEntity;
import me.imbuzz.dev.petsreloaded.core.PetsReloaded;
import me.imbuzz.dev.petsreloaded.core.commands.PetsCommand;
import me.imbuzz.dev.petsreloaded.core.utils.Strings;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PetsSit implements PetsCommand {

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission(getPermission());
    }

    @Override
    public String getPermission() {
        return "pets.sit";
    }

    @Override
    public String getDescription() {
        return "Decide if your pet has to move or not";
    }

    @Override
    public String getUsage() {
        return "pets sit";
    }

    @Override
    public boolean onCommand(PetsReloaded petsReloaded, CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Strings.colorize(
                    petsReloaded.getVariablesContainer().getCannotExecuteThisCommand()));
            return false;
        }

        Player player = ((Player) sender);

        if (!petsReloaded.getPetsManager().hasActivePet(player)) {
            sender.sendMessage(Strings.colorize(petsReloaded.getVariablesContainer().getNeedActivePet()));
            return false;
        }

        IPetEntity petEntity = petsReloaded.getPetsManager().getPet(player);

        PetChangeSitEvent sitEvent = new PetChangeSitEvent(petEntity, petEntity.getPositionType(), petEntity.getPositionType().next());
        Bukkit.getPluginManager().callEvent(sitEvent);

        if (!sitEvent.isCancelled()) {
            petEntity.setPositionType(sitEvent.getNewPosition());

            if (sitEvent.getNewPosition() == PetChangeSitEvent.PositionType.UNBLOCKED) {
                sender.sendMessage(Strings.colorize(
                        petsReloaded.
                                getVariablesContainer().getBlockedOff().replace("%pet%", petEntity.getPetSettings().getPetName())
                ));
            } else {
                sender.sendMessage(Strings.colorize(petsReloaded
                        .getVariablesContainer().getBlockedOn().replace("%pet%", petEntity.getPetSettings().getPetName())));
            }
        }

        return true;
    }

}
