package me.imbuzz.dev.petsreloaded.nms.v1_13_R1;

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
                return new ItemStack(Material.PLAYER_HEAD);
            }
        }
        return itemStack;
    }

    @Override
    public ItemStack applyCustomModelDataOnItem(ItemStack itemStack, int modelInt) {
        return itemStack;
    }

}
