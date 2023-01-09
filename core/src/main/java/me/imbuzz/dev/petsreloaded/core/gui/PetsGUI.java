package me.imbuzz.dev.petsreloaded.core.gui;

import com.cryptomorin.xseries.XSound;
import com.google.common.collect.Lists;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import me.imbuzz.dev.petsreloaded.core.PetsReloaded;
import me.imbuzz.dev.petsreloaded.core.files.LanguageFile;
import me.imbuzz.dev.petsreloaded.core.managers.PetsManager;
import me.imbuzz.dev.petsreloaded.core.objects.pets.settings.EntitySettings;
import me.imbuzz.dev.petsreloaded.core.utils.Strings;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PetsGUI implements InventoryProvider {
    private static SmartInventory INVENTORY;
    private final PetsReloaded petsReloaded;
    private final PetsManager petsManager;

    public PetsGUI(PetsReloaded petsReloaded) {
        this.petsReloaded = petsReloaded;
        petsManager = this.petsReloaded.getPetsManager();
    }

    public static SmartInventory getInventory(PetsReloaded petsReloaded) {
        INVENTORY = SmartInventory.builder()
                .provider(new PetsGUI(petsReloaded))
                .size(6, 9)
                .manager(petsReloaded.getInventoryManager())
                .title(ChatColor.DARK_GRAY + Strings.colorize(petsReloaded.getLanguage().getProperty(LanguageFile.MENU_TITLE)))
                .build();
        return INVENTORY;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        Pagination pagination = contents.pagination();
        List<ClickableItem> currentItems = getPetItems(player, contents);

        ClickableItem[] items = new ClickableItem[currentItems.size()];

        for (int i = 0; i < items.length; i++) items[i] = currentItems.get(i);

        pagination.setItems(items);
        pagination.setItemsPerPage(21);
        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1)
                .blacklist(1, 8)
                .blacklist(2, 0)
                .blacklist(3, 0)

                .blacklist(2, 8)
                .blacklist(3, 8)

                .blacklist(4, 0)
                .blacklist(4, 1)
                .blacklist(4, 2)
                .blacklist(4, 3)
                .blacklist(4, 4)
                .blacklist(4, 5)
                .blacklist(4, 6)
                .blacklist(4, 7)
                .blacklist(4, 8)

                .blacklist(5, 0)
                .blacklist(5, 1)
                .blacklist(5, 2)
                .blacklist(5, 3)
                .blacklist(5, 4)
                .blacklist(5, 5)
                .blacklist(5, 6)
                .blacklist(5, 7)
                .blacklist(5, 8)
        );

        if (currentItems.size() > 21) {
            if (!pagination.isFirst()) {
                contents.set(5, 3, ClickableItem.of(petsReloaded.getVariablesContainer().getBackItem(), e -> {

                    INVENTORY.open(player, pagination.previous().getPage());
                    player.playSound(player.getLocation(), XSound.ENTITY_ITEM_PICKUP.parseSound(), 1, 1);
                }));
            }

            if (!pagination.isLast()) {
                contents.set(5, 5, ClickableItem.of(petsReloaded.getVariablesContainer().getForwardItem(), e -> {
                    INVENTORY.open(player, pagination.next().getPage());
                    player.playSound(player.getLocation(), XSound.ENTITY_ITEM_PICKUP.parseSound(), 1, 1);
                }));
            }

        }

        createCloseItem(player, contents);
    }

    private void createCloseItem(Player player, InventoryContents contents) {
        contents.set(5, 4, ClickableItem.of(petsReloaded.getVariablesContainer().getCloseItem(), e -> player.closeInventory()));
    }

    private List<ClickableItem> getPetItems(Player player, InventoryContents contents) {
        List<ClickableItem> items = Lists.newArrayList();

        for (EntitySettings value : petsManager.getPetsPresets().values()) {
            boolean unlocked = player.hasPermission(value.getPermission()) || player.hasPermission("petsreloaded.pets.*");

            ItemStack item = unlocked ? value.getSymbols().get("unlocked_symbol").clone() : value.getSymbols().get("locked_symbol").clone();

            items.add(ClickableItem.of(item, event -> {
                if (unlocked) {
                    spawnPet(value, player);
                    init(player, contents);
                } else
                    player.sendMessage(Strings.colorize(petsReloaded.getVariablesContainer().getBuyPetMessage()));
            }));
        }

        return items;
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    public void spawnPet(EntitySettings settings, Player player) {
        if (petsManager.hasActivePet(player)) {
            String[] returnM = petsManager.despawnPet(player);
            if (returnM.length > 0) {
                if (settings.getTag().equalsIgnoreCase(returnM[0])) {
                    player.sendMessage(Strings.colorize(petsReloaded.getVariablesContainer().getDespawnPetMessage()
                            .replace("%pet%", returnM[1]))
                            .replace("%player%", player.getName()));
                    return;
                }
            }
        }

        if (PetsReloaded.get().getVariablesContainer().getDisabledWorlds().contains(player.getWorld().getName())) {
            player.sendMessage(Strings.colorize(PetsReloaded.get().getVariablesContainer().getTeleport_to_disabled_world()
                    .replace("%pet%", settings.getPetName()))
                    .replace("%player%", player.getName()));
            return;
        }

        petsManager.spawnPet(settings.getTag(), player);
        player.sendMessage(Strings.colorize(petsReloaded.getVariablesContainer().getSpawnPetMessage()
                .replace("%pet%", settings.getPetName()))
                .replace("%player%", player.getName()));
    }


}
