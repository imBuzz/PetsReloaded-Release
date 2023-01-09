package me.imbuzz.dev.petsreloaded.core.nms.interfacing;

import me.imbuzz.dev.petsreloaded.core.objects.pets.PetEntity;
import me.imbuzz.dev.petsreloaded.core.objects.pets.enums.PetStatus;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public interface IEntityHandler {

    Entity spawnBase(PetEntity petEntity, Entity owner, Location location);

    void updatePetStatus(Entity entity, PetStatus... petStatuses);

    default void registerEntities() {
    }

    default void addToMaps(Class clazz, String name, int id) {
    }

}
