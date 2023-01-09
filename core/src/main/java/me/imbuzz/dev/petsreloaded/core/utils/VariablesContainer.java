package me.imbuzz.dev.petsreloaded.core.utils;

import com.cryptomorin.xseries.XMaterial;
import lombok.Getter;
import me.imbuzz.dev.petsreloaded.core.PetsReloaded;
import me.imbuzz.dev.petsreloaded.core.files.LanguageFile;
import me.imbuzz.dev.petsreloaded.core.files.SettingsFile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;

public class VariablesContainer {

    private final PetsReloaded petsReloaded;

    @Getter
    private final String spawnPetMessage, teleport_to_disabled_world,
            despawnPetMessage,
            reloadMessage, buyPetMessage,
            loadingPets, noPermissionMessage,
            cannotExecuteThisCommand,
            reloadingMessage, clearWorldCompleted,
            clearGlobal, blockedOn, blockedOff, needActivePet, prefix,
            commandError, invalidPet, noPetActive, petRename, inappropriateWord, maxLengthForRename;

    @Getter
    private final int maxCharsRename;
    @Getter
    private final boolean enableRidingName, canRide, canInteract, spawnToLastLocation;
    @Getter
    private ItemStack backItem = new ItemStack(Material.STONE), forwardItem = new ItemStack(Material.STONE), closeItem = new ItemStack(Material.STONE);
    @Getter
    private final List<String> disabledWorlds, blacklistedWordsFromRename;

    public VariablesContainer(PetsReloaded plugin) {
        petsReloaded = plugin;

        Optional<XMaterial> closeMaterial = XMaterial.matchXMaterial(petsReloaded.getLanguage().getProperty(LanguageFile.MENU_CLOSE_ITEM_MATERIAL));
        if (closeMaterial.isPresent()) {
            closeItem = new ItemBuilder(petsReloaded, closeMaterial.get().parseItem(), closeMaterial.get().toString()).setName(Strings.colorize(
                            petsReloaded.getLanguage().getProperty(LanguageFile.MENU_CLOSE_ITEM_NAME)))
                    .addLoreLines(Strings.colorize(
                            petsReloaded.getLanguage().getProperty(LanguageFile.MENU_CLOSE_ITEM_LORE)))
                    .build();
        } else {
            Bukkit.getConsoleSender().sendMessage(Strings.colorize("&cCannot load close item for Pets Menu ...setting STONE"));
        }

        Optional<XMaterial> forwardMaterial = XMaterial.matchXMaterial(petsReloaded.getLanguage().getProperty(LanguageFile.MENU_FORWARD_ITEM_MATERIAL));
        if (forwardMaterial.isPresent()) {
            forwardItem = new ItemBuilder(petsReloaded, forwardMaterial.get().parseItem(), forwardMaterial.get().toString()).setName(Strings.colorize(
                            petsReloaded.getLanguage().getProperty(LanguageFile.MENU_FORWARD_ITEM_NAME)))
                    .addLoreLines(Strings.colorize(
                            petsReloaded.getLanguage().getProperty(LanguageFile.MENU_FORWARD_ITEM_LORE)))
                    .build();
        } else {
            Bukkit.getConsoleSender().sendMessage(Strings.colorize("&cCannot load forward item for Pets Menu ...setting STONE"));
        }

        Optional<XMaterial> backMaterial = XMaterial.matchXMaterial(petsReloaded.getLanguage().getProperty(LanguageFile.MENU_BACK_ITEM_MATERIAL));
        if (backMaterial.isPresent()) {
            backItem = new ItemBuilder(petsReloaded, backMaterial.get().parseItem(), backMaterial.get().toString()).setName(Strings.colorize(
                            petsReloaded.getLanguage().getProperty(LanguageFile.MENU_BACK_ITEM_NAME)))
                    .addLoreLines(Strings.colorize(
                            petsReloaded.getLanguage().getProperty(LanguageFile.MENU_BACK_ITEM_LORE)))
                    .build();
        } else {
            Bukkit.getConsoleSender().sendMessage(Strings.colorize("&cCannot load back item for Pets Menu ...setting STONE"));
        }


        maxCharsRename = petsReloaded.getSettings().getProperty(SettingsFile.MAX_CHARS_FOR_RENAME);
        enableRidingName = petsReloaded.getSettings().getProperty(SettingsFile.ENABLE_REMOVE_NAME_WHEN_RIDING);
        canRide = petsReloaded.getSettings().getProperty(SettingsFile.CAN_RIDE);
        canInteract = petsReloaded.getSettings().getProperty(SettingsFile.ENABLE_INTERACT);
        spawnToLastLocation = petsReloaded.getSettings().getProperty(SettingsFile.SPAWN_ON_LAST_LOCATION);
        disabledWorlds = petsReloaded.getSettings().getProperty(SettingsFile.DISABLED_WORLDS);
        blacklistedWordsFromRename = petsReloaded.getSettings().getProperty(SettingsFile.BLACKLISTED_WORDS_FROM_RENAME);

        prefix = petsReloaded.getLanguage().getProperty(LanguageFile.PREFIX);

        //SUMMON - UNLOCK
        spawnPetMessage = petsReloaded.getLanguage().getProperty(LanguageFile.SUMMON_SPAWN).replace("%prefix%", prefix);
        despawnPetMessage = petsReloaded.getLanguage().getProperty(LanguageFile.SUMMON_DESPAWN).replace("%prefix%", prefix);
        teleport_to_disabled_world = petsReloaded.getLanguage().getProperty(LanguageFile.REMOVED_PET_FOR_DISABLED_WORLD).replace("%prefix%", prefix);
        buyPetMessage = petsReloaded.getLanguage().getProperty(LanguageFile.UNLOCK_AT).replace("%prefix%", prefix);

        //DEBUG
        commandError = petsReloaded.getLanguage().getProperty(LanguageFile.DEBUG_COMMAND_ERROR).replace("%prefix%", prefix);
        loadingPets = petsReloaded.getLanguage().getProperty(LanguageFile.DEBUG_LOADING).replace("%prefix%", prefix);
        reloadMessage = petsReloaded.getLanguage().getProperty(LanguageFile.DEBUG_RELOADED).replace("%prefix%", prefix);
        reloadingMessage = petsReloaded.getLanguage().getProperty(LanguageFile.DEBUG_RELOADING).replace("%prefix%", prefix);

        //COMMANDS
        noPetActive = petsReloaded.getLanguage().getProperty(LanguageFile.NO_PET_ACTIVE).replace("%prefix%", prefix);
        invalidPet = petsReloaded.getLanguage().getProperty(LanguageFile.PETS_DOES_NOT_EXIST).replace("%prefix%", prefix);
        noPermissionMessage = petsReloaded.getLanguage().getProperty(LanguageFile.NOT_PERMISSION).replace("%prefix%", prefix);
        cannotExecuteThisCommand = petsReloaded.getLanguage().getProperty(LanguageFile.CANNOT_EXECUTE).replace("%prefix%", prefix);
        clearGlobal = petsReloaded.getLanguage().getProperty(LanguageFile.GLOBAL_CLEAR).replace("%prefix%", prefix);
        clearWorldCompleted = petsReloaded.getLanguage().getProperty(LanguageFile.CLEAR_WORLD).replace("%prefix%", prefix);
        blockedOn = petsReloaded.getLanguage().getProperty(LanguageFile.PET_BLOCKED).replace("%prefix%", prefix);
        blockedOff = petsReloaded.getLanguage().getProperty(LanguageFile.PET_UNBLOCKED).replace("%prefix%", prefix);
        needActivePet = petsReloaded.getLanguage().getProperty(LanguageFile.NEED_ACTIVE_PET).replace("%prefix%", prefix);
        petRename = petsReloaded.getLanguage().getProperty(LanguageFile.CHANGED_NAME).replace("%prefix%", prefix);
        inappropriateWord = petsReloaded.getLanguage().getProperty(LanguageFile.CONTAINS_BLACKLISTED_WORD).replace("%prefix%", prefix);
        maxLengthForRename = petsReloaded.getLanguage().getProperty(LanguageFile.MAX_LENGTH_REACHED_FOR_RENAME).replace("%prefix%", prefix);
    }


}
