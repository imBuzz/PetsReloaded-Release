package me.imbuzz.dev.petsreloaded.core.utils;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.imbuzz.dev.petsreloaded.core.PetsReloaded;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Color;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.*;

public class ItemBuilder {

    private ItemStack item;
    private ItemMeta meta;

    public ItemBuilder(PetsReloaded petsReloaded, ItemStack itemStack, String material) {
        item = petsReloaded.getNmsHandler().getItemHandler().adjustMaterial(itemStack, material.replace(" ", "_").toUpperCase(Locale.ROOT));
        meta = item.getItemMeta();
    }

    public String getName() {
        return meta.hasDisplayName() ? meta.getDisplayName() : Strings.colorize("&f" + WordUtils.capitalize(item.getType().toString().toLowerCase().replace("_", " ")));
    }

    public ItemBuilder setName(String name) {
        meta.setDisplayName(Strings.colorize(name));
        return this;
    }

    public ItemBuilder setDurability(int damage) {
        item.setDurability((short) damage);
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        return setLore(Arrays.asList(lore));
    }

    public ItemBuilder setLore(List<String> lore) {
        meta.setLore(Strings.colorize(lore));
        return this;
    }

    public List<String> getLore() {
        return meta.getLore();
    }

    public Map<Enchantment, Integer> getEnchantments() {
        Map<Enchantment, Integer> values = Maps.newHashMap();
        values.putAll(meta.getEnchants());
        return values;
    }

    public ItemBuilder addLore(List<String> lores) {
        List<String> newLore = meta.getLore();
        newLore.addAll(lores);

        meta.setLore(newLore);

        return this;
    }

    public ItemBuilder setFlags(ItemFlag... flags) {
        for (ItemFlag flag : flags)
            meta.addItemFlags(flag);
        return this;
    }

    public ItemBuilder addEnchant(Enchantment ench, int level) {
        meta.addEnchant(ench, level, true);
        return this;
    }

    public ItemBuilder setSkull(String value) {
        SkullMeta meta = (SkullMeta) this.meta;
        GameProfile profile = new GameProfile(UUID.randomUUID(), "");
        profile.getProperties().put("textures", new Property("textures", value));
        Field profileField;
        try {
            profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        this.meta = meta;
        return this;
    }

    public ItemBuilder setPlayerSkull(String playerName) {
        SkullMeta meta = (SkullMeta) this.meta;
        meta.setOwner(playerName);
        this.meta = meta;
        return this;
    }

    public ItemBuilder removeLoreLines(List<String> lines) {
        List<String> lore = new ArrayList<>();
        if (meta.hasLore()) lore = new ArrayList<>(meta.getLore());

        for (String s : meta.getLore()) {
            for (String line : lines) {
                if (s.replaceAll("§", "&").equalsIgnoreCase(line)) lore.remove(s);
            }
        }

        meta.setLore(lore);
        return this;
    }

    public ItemBuilder removeLoreLines(String... lines) {
        List<String> lore = new ArrayList<>();
        if (meta.hasLore()) lore = new ArrayList<>(meta.getLore());

        for (String s : meta.getLore()) {
            for (String line : lines) {
                if (s.replaceAll("§", "&").equalsIgnoreCase(line)) lore.remove(s);
            }
        }

        meta.setLore(lore);
        return this;
    }

    public ItemBuilder addLoreLines(List<String> lines) {
        List<String> lore = new ArrayList<>();
        if (meta.hasLore()) lore = new ArrayList<>(meta.getLore());
        for (String line : lines) {
            lore.add(Strings.colorize(line));
        }

        meta.setLore(lore);
        return this;
    }

    public ItemBuilder setUnbreakable(boolean state) {
        meta.spigot().setUnbreakable(state);
        return this;
    }

    public ItemBuilder setLeatherColor(int red, int green, int blue) {
        LeatherArmorMeta im = (LeatherArmorMeta) meta;
        im.setColor(Color.fromRGB(red, green, blue));
        return this;
    }

    public ItemBuilder setLeatherColor(Color color) {
        LeatherArmorMeta im = (LeatherArmorMeta) meta;
        im.setColor(color);
        return this;
    }


    public ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }


}
