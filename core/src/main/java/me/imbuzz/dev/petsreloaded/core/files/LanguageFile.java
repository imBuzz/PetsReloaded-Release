package me.imbuzz.dev.petsreloaded.core.files;

import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.Property;

import java.util.List;

import static ch.jalu.configme.properties.PropertyInitializer.newListProperty;
import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class LanguageFile implements SettingsHolder {

    public static final Property<String> MENU_TITLE = newProperty("gui.menu.title", "&8Pets");
    public static final Property<String> MENU_BACK_ITEM_MATERIAL = newProperty("gui.menu.backItem.material", "ARROW");
    public static final Property<String> MENU_BACK_ITEM_NAME = newProperty("gui.menu.backItem.name", "&7Back");
    public static final Property<List<String>> MENU_BACK_ITEM_LORE = newListProperty("gui.menu.backItem.lore", "&7Go Back");

    public static final Property<String> MENU_FORWARD_ITEM_MATERIAL = newProperty("gui.menu.forwardItem.material", "ARROW");
    public static final Property<String> MENU_FORWARD_ITEM_NAME = newProperty("gui.menu.forwardItem.name", "&7Forward");
    public static final Property<List<String>> MENU_FORWARD_ITEM_LORE = newListProperty("gui.menu.forwardItem.lore", "&7Go Forward");

    public static final Property<String> MENU_CLOSE_ITEM_MATERIAL = newProperty("gui.menu.closeItem.material", "BARRIER");
    public static final Property<String> MENU_CLOSE_ITEM_NAME = newProperty("gui.menu.closeItem.name", "&7Close");
    public static final Property<List<String>> MENU_CLOSE_ITEM_LORE = newListProperty("gui.menu.closeItem.lore", "&7Close menu");

    public static final Property<String> PREFIX = newProperty("messages.prefix", "&c&lPetsReloaded&7:");

    public static final Property<String> REMOVED_PET_FOR_DISABLED_WORLD = newProperty("messages.summon.teleport-to-disabled-world", "%prefix% &cYou can't use a pet in this world");
    public static final Property<String> SUMMON_SPAWN = newProperty("messages.summon.spawn", "%prefix% &aSpawned your &e%pet% &apet!");
    public static final Property<String> SUMMON_DESPAWN = newProperty("messages.summon.despawn", "%prefix% &cDespawned your &e%pet% &cpet!");
    public static final Property<String> PETS_DOES_NOT_EXIST = newProperty("messages.commands.summon.pet_not_exist", "%prefix% &cThe pet called: %pet% does not exist");

    public static final Property<String> UNLOCK_AT = newProperty("messages.unlock.buy_pet_at", "%prefix% &bTo unlock this pet, you need to buy it on example.example.it");

    public static final Property<String> DEBUG_COMMAND_ERROR = newProperty("messages.debug.command_error", "%prefix% &cError while executing this command!");
    public static final Property<String> DEBUG_LOADING = newProperty("messages.debug.loading_pets", "%prefix% &eWe are currently loading all pets, please wait...");
    public static final Property<String> DEBUG_RELOADING = newProperty("messages.debug.reloading", "%prefix% &eReloading...");
    public static final Property<String> DEBUG_RELOADED = newProperty("messages.debug.reloaded", "%prefix% &aReload configuration completed!");

    public static final Property<String> NOT_PERMISSION = newProperty("messages.commands.general.not_permission", "%prefix% &cYou don't have the permission to do that!");
    public static final Property<String> CANNOT_EXECUTE = newProperty("messages.commands.general.entity_cannot_execute_this_command", "%prefix% &cYou cannot execute this command!");
    public static final Property<String> NEED_ACTIVE_PET = newProperty("messages.commands.general.need_active_pet_to_do_that", "%prefix% &cYou need an active pet to do this action!");
    public static final Property<String> NO_PET_ACTIVE = newProperty("messages.commands.general.no_pet_active", "%prefix% &cYou don't have any active pet!");

    public static final Property<String> CLEAR_WORLD = newProperty("messages.commands.clear.worldCompleted", "%prefix% &aCleared all pets in the world: %world%");
    public static final Property<String> GLOBAL_CLEAR = newProperty("messages.commands.clear.global", "%prefix% &aCleared all pets in the server");

    public static final Property<String> PET_BLOCKED = newProperty("messages.commands.block.blocked", "%prefix% &cYour &e%pet% &cpet is now blocked");
    public static final Property<String> PET_UNBLOCKED = newProperty("messages.commands.block.not_blocked", "%prefix% &aYour &e%pet% &apet is now unblocked");

    public static final Property<String> CHANGED_NAME = newProperty("messages.commands.name.rename_pet", "%prefix% &aYou have successfully renamed your pet!");
    public static final Property<String> CONTAINS_BLACKLISTED_WORD = newProperty("messages.commands.name.blacklisted_word", "%prefix% &cThis name contains an inappropriate word!");
    public static final Property<String> MAX_LENGTH_REACHED_FOR_RENAME = newProperty("messages.commands.name.max_length_reached_for_rename",
            "%prefix% &cYou are allowed to enter a max of {max} characters on new pet names");
}
