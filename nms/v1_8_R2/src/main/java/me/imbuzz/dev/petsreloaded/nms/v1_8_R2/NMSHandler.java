package me.imbuzz.dev.petsreloaded.nms.v1_8_R2;

import lombok.Getter;
import me.imbuzz.dev.petsreloaded.core.PetsReloaded;
import me.imbuzz.dev.petsreloaded.core.nms.INMSHandler;

public class NMSHandler implements INMSHandler {

    @Getter
    private final PetsReloaded plugin;
    @Getter
    private final EntityHandler entityHandler;
    @Getter
    private final ItemHandler itemHandler;

    public NMSHandler(PetsReloaded plugin) {
        this.plugin = plugin;

        entityHandler = new EntityHandler(this);
        itemHandler = new ItemHandler(this);
    }



}
