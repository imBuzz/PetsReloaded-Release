package me.imbuzz.dev.petsreloaded.nms.v1_9_R2;

import lombok.RequiredArgsConstructor;
import me.imbuzz.dev.petsreloaded.core.nms.interfacing.IItemHandler;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class ItemHandler implements IItemHandler {
    private final NMSHandler nmsHandler;

    @Override
    public ItemStack adjustMaterial(ItemStack itemStack, String material) {
        switch (material) {
            case "PLAYER_HEAD": {
                return new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            }
        }
        return itemStack;
    }

    @Override
    public ItemStack applyCustomModelDataOnItem(ItemStack itemStack, int modelInt) {
        return itemStack;
    }

}
