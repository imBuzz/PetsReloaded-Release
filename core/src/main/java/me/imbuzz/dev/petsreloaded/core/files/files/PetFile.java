package me.imbuzz.dev.petsreloaded.core.files.files;

import com.cryptomorin.xseries.XMaterial;
import lombok.Getter;
import me.imbuzz.dev.petsreloaded.core.PetsReloaded;
import me.imbuzz.dev.petsreloaded.core.objects.pets.settings.EntitySettings;
import me.imbuzz.dev.petsreloaded.core.utils.FileUtils;
import me.imbuzz.dev.petsreloaded.core.utils.ItemBuilder;
import me.imbuzz.dev.petsreloaded.core.utils.ItemTools;
import me.imbuzz.dev.petsreloaded.core.utils.Strings;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.Locale;
import java.util.Optional;

public class PetFile {

    private final File file;
    @Getter
    private final FileConfiguration data;
    @Getter
    EntitySettings entitySettings;

    public PetFile(File file) {
        this.file = file;
        data = YamlConfiguration.loadConfiguration(file);
    }

    public EntitySettings load(PetsReloaded plugin) {
        try {
            entitySettings = new EntitySettings();
            entitySettings.setTag(FilenameUtils.removeExtension(file.getName()));

            //entitySettings.setTag(ChatColor.stripColor(Strings.colorize(data.getString("information.petName"))).toLowerCase(Locale.ROOT).replace(" ", "_"));
            entitySettings.setSpeed(data.getDouble("information.speed") > 0 ? data.getDouble("information.speed") : 1.3);
            entitySettings.setDistanceInBlock(data.getDouble("information.max_distance") > 0 ? data.getDouble("information.max_distance") : 10);

            if (!data.getBoolean("information.mountable"))
                entitySettings.setMountable(false);

            entitySettings.setVisibile(data.getBoolean("structure.visible"));

            String entityType = FileUtils.getString(data, "structure.entity");
            if (entityType != null && !entityType.isEmpty()) {
                try {
                    entitySettings.setEntityType(EntityType.valueOf(entityType));
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }


            entitySettings.setModelName(data.getString("information.model"));
            entitySettings.setPetName(data.getString("information.petName"));
            entitySettings.setDescription(Strings.colorize(FileUtils.getStringList(data, "information.description")));
            entitySettings.setPermission(FileUtils.getString(data, "information.permission"));

            EntitySettings.StructureSettings nameEntity = new EntitySettings.StructureSettings();
            nameEntity.displayName = FileUtils.getString(data, "information.displayName.name");
            nameEntity.yOffset = data.getDouble("information.displayName.yOffset");
            nameEntity.tag = "pet_display_name";
            entitySettings.getStructureSettings().put("pet_display_name", nameEntity);

            ConfigurationSection symbols = data.getConfigurationSection("information.symbols");
            if (symbols != null) {
                for (String key : symbols.getKeys(false)) {
                    if (key == null) continue;

                    entitySettings.getSymbols().put(key, getItem(plugin, symbols, key));
                }
            }

            ConfigurationSection items = data.getConfigurationSection("items");
            if (items != null) {
                for (String itemKey : items.getKeys(false)) {
                    if (itemKey == null) continue;

                    entitySettings.getItems().put(itemKey, getItem(plugin, items, itemKey));
                }
            }

            ConfigurationSection structure = data.getConfigurationSection("structure.stands");
            if (structure != null) {
                for (String standKey : structure.getKeys(false)) {
                    EntitySettings.StructureSettings structureSettings = new EntitySettings.StructureSettings();

                    structureSettings.modelName = structure.getString(standKey + ".model");
                    structureSettings.helmet = structure.getString(standKey + ".helmet");
                    structureSettings.chestplate = structure.getString(standKey + ".chestplate");
                    structureSettings.legs = structure.getString(standKey + ".legs");
                    structureSettings.boots = structure.getString(standKey + ".boots");
                    structureSettings.handItem = structure.getString(standKey + ".handItem");

                    structureSettings.small = structure.getBoolean(standKey + ".small");
                    structureSettings.visibile = structure.getBoolean(standKey + ".visibile");
                    structureSettings.plate = structure.getBoolean(standKey + ".plate");

                    structureSettings.xOffset = structure.getDouble(standKey + ".location.xOffset");
                    structureSettings.yOffset = structure.getDouble(standKey + ".location.yOffset");
                    structureSettings.zOffset = structure.getDouble(standKey + ".location.zOffset");


                    structureSettings.headPos = ItemTools.getAngleFromString(FileUtils.getString(structure, standKey + ".positions.headPos"));
                    structureSettings.bodyPos = ItemTools.getAngleFromString(FileUtils.getString(structure, standKey + ".positions.bodyPos"));
                    structureSettings.leftArmPos = ItemTools.getAngleFromString(FileUtils.getString(structure, standKey + ".positions.leftArmPos"));
                    structureSettings.rightArmPos = ItemTools.getAngleFromString(FileUtils.getString(structure, standKey + ".positions.rightArmPos"));
                    structureSettings.leftLegsPos = ItemTools.getAngleFromString(FileUtils.getString(structure, standKey + ".positions.leftLegsPos"));
                    structureSettings.rightLegsPos = ItemTools.getAngleFromString(FileUtils.getString(structure, standKey + ".positions.rightLegsPos"));

                    structureSettings.tag = standKey;

                    entitySettings.getStructureSettings().put(standKey, structureSettings);
                }
            }

            return entitySettings;
        } catch (Exception e) {
            plugin.getLogger().severe("Error to load the pet in the file called: " + file.getName());
            e.printStackTrace();
        }


        return null;
    }

    private ItemStack getItem(PetsReloaded petsReloaded, ConfigurationSection section, String key) {
        Optional<XMaterial> optionalXMaterial = XMaterial.matchXMaterial(FileUtils.getString(section, key + ".material"));
        if (!optionalXMaterial.isPresent()) {
            petsReloaded.getLogger().severe("Cannot load item: " + key + " from " + file.getName() + " stopping...");
            return null;
        }

        XMaterial material = optionalXMaterial.get();
        String stringMaterial = material.toString().replace(" ", "_").toUpperCase(Locale.ROOT);
        ItemBuilder itemBuilder = new ItemBuilder(petsReloaded, material.parseItem(), stringMaterial);

        if (stringMaterial.equalsIgnoreCase("PLAYER_HEAD")) {
            String skull = FileUtils.getString(section, key + ".skull");
            if (skull.length() > 16) itemBuilder.setSkull(FileUtils.getString(section, key + ".skull"));
            else itemBuilder.setPlayerSkull(skull);
        }

        if (stringMaterial.contains("LEATHER_")) {
            if (section.getString(key + ".color") != null) {
                itemBuilder.setLeatherColor(ItemTools.getColorFromString(FileUtils.getString(section, key + ".color")));
            }
        }

        itemBuilder.addLoreLines(FileUtils.getStringList(section, key + ".lore"));
        itemBuilder.setName(FileUtils.getString(section, key + ".name"));

        ItemStack itemStack = itemBuilder.build();

        int modelData = section.getInt(key + ".model");
        if (modelData > 0)
            itemStack = petsReloaded.getNmsHandler().getItemHandler().applyCustomModelDataOnItem(itemStack, modelData);

        return itemStack;
    }


}
