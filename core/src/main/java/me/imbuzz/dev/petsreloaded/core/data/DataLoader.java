package me.imbuzz.dev.petsreloaded.core.data;

import org.bukkit.entity.Player;

public interface DataLoader {

    boolean init();

    LoaderType getType();

    void loadPlayer(Player player);

    void savePlayer(Player player);

    void saveAllPlayers();
}
