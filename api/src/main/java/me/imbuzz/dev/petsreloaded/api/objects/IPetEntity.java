package me.imbuzz.dev.petsreloaded.api.objects;

import me.imbuzz.dev.petsreloaded.api.events.PetChangeSitEvent;
import me.imbuzz.dev.petsreloaded.api.managers.IPetSettings;

public interface IPetEntity {

    /**
     * Return the settings for that pet, for example the pet tag,
     * name and permission needed to use it.
     *
     * @return the settings of the current pet.
     */
    IPetSettings getPetSettings();

    /**
     * @return the owner of that pet.
     */
    String getOwner();

    /**
     * Check if a pet cannot move
     *
     * @return true is it can move, false if it cannot.
     */
    boolean isBlocked();

    /**
     * @return the current pet movement state (BLOCKED, UNBLOCKED)
     */
    PetChangeSitEvent.PositionType getPositionType();

    /**
     * @param position Set the current pet movement state
     */
    void setPositionType(PetChangeSitEvent.PositionType position);

    /**
     * @param state true to show the pet's name
     */
    void enableName(boolean state);


}
