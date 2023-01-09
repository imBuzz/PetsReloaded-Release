package me.imbuzz.dev.petsreloaded.core.nms;

import me.imbuzz.dev.petsreloaded.core.nms.interfacing.IEntityHandler;
import me.imbuzz.dev.petsreloaded.core.nms.interfacing.IItemHandler;

public interface INMSHandler {

    IEntityHandler getEntityHandler();

    IItemHandler getItemHandler();

}
