package me.imbuzz.dev.petsreloaded.core.objects.player;

import lombok.Getter;
import me.imbuzz.dev.petsreloaded.core.objects.pets.PetEntity;

import java.util.HashMap;
import java.util.UUID;

@Getter
public class PetsPlayer {

    private final UUID uuid;
    private final HashMap<String, String> petNames;
    private String activePetID = "null";

    private transient PetEntity activePet;

    public PetsPlayer(UUID uuid) {
        this.uuid = uuid;
        petNames = new HashMap<>();
    }

    public PetsPlayer(UUID uuid, HashMap<String, String> petNames) {
        this.uuid = uuid;
        this.petNames = petNames;
    }

    public void setActivePet(PetEntity activePet) {
        this.activePet = activePet;
        if (this.activePet == null) activePetID = "null";
        else activePetID = this.activePet.getPetSettings().getTag();
    }
}
