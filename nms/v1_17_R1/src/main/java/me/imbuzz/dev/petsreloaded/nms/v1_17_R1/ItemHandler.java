package me.imbuzz.dev.petsreloaded.nms.v1_17_R1;

import lombok.RequiredArgsConstructor;
import me.imbuzz.dev.petsreloaded.core.nms.interfacing.IItemHandler;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return itemStack;
        meta.setCustomModelData(modelInt);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

}
