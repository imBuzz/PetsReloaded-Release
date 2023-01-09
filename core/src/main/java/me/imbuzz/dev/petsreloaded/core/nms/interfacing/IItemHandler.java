package me.imbuzz.dev.petsreloaded.core.nms.interfacing;

import org.bukkit.inventory.ItemStack;

public interface IItemHandler {

    ItemStack adjustMaterial(ItemStack itemStack, String material);

    ItemStack applyCustomModelDataOnItem(ItemStack itemStack, int modelInt);

}
