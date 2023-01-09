package me.imbuzz.dev.petsreloaded.api.managers;

import me.imbuzz.dev.petsreloaded.api.objects.IPetEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public interface IPetsManager {

    /**
     * Check if a player has an active pet
     *
     * @param player to check
     * @return true if he has an active pet, false if he has not.
     */
    boolean hasActivePet(Player player);

    /**
     * Get the current active pet of a player
     *
     * @param player to check
     * @return the current active pet, returns null if the player has not an active pet
     */
    @Nullable
    IPetEntity getPet(Player player);

    /**
     * Despawn an active pet for a player
     *
     * @param player
     * @return the pet tag and the name in an Array
     */
    String[] despawnPet(Player player);

    /**
     * Despawn an active pet for an unknown player
     *
     * @param pet to despawn
     * @return the pet tag and the name in an Array
     */
    String[] despawnPet(IPetEntity pet);

    /**
     * Spawn a pet for a player
     *
     * @param petTag
     * @param player who own that pet
     */
    void spawnPet(String petTag, Player player);


}
