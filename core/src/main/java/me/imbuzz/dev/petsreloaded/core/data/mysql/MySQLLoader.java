package me.imbuzz.dev.petsreloaded.core.data.mysql;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.imbuzz.dev.petsreloaded.api.events.PetChangeSitEvent;
import me.imbuzz.dev.petsreloaded.core.PetsReloaded;
import me.imbuzz.dev.petsreloaded.core.data.DataLoader;
import me.imbuzz.dev.petsreloaded.core.data.LoaderType;
import me.imbuzz.dev.petsreloaded.core.data.mysql.utils.MySQL;
import me.imbuzz.dev.petsreloaded.core.files.SettingsFile;
import me.imbuzz.dev.petsreloaded.core.objects.player.PetsPlayer;
import me.imbuzz.dev.petsreloaded.core.utils.Strings;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.HashMap;

public class MySQLLoader implements DataLoader {

    private static final Gson GSON = new Gson();
    private final PetsReloaded plugin = PetsReloaded.get();
    private final String TABLE = plugin.getSettings().getProperty(SettingsFile.MSQL_TABLE);

    private MySQL mySQl;

    @Override
    public boolean init() {
        try {
            mySQl = new MySQL(plugin.getSettings().getProperty(SettingsFile.MSQL_HOSTNAME),
                    plugin.getSettings().getProperty(SettingsFile.MSQL_PORT), plugin.getSettings().getProperty(SettingsFile.MSQL_DATABASE_NAME),
                    plugin.getSettings().getProperty(SettingsFile.MSQL_USERNAME), plugin.getSettings().getProperty(SettingsFile.MSQL_PASSWORD));

            mySQl.createTable(new String[]{
                    "uuid VARCHAR(36) PRIMARY KEY",
                    "active_pet VARCHAR(25)",
                    "blocked BOOLEAN",
                    "last_location VARCHAR(3000)",
                    "pets_names VARCHAR(8000)",
            }, TABLE);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public LoaderType getType() {
        return LoaderType.MYSQL;
    }

    @Override
    public void loadPlayer(Player player) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            try {
                PetsPlayer petsPlayer;
                if (mySQl.rowExists("uuid", player.getUniqueId(), TABLE)) {
                    petsPlayer = new PetsPlayer(player.getUniqueId(),
                            GSON.fromJson(mySQl.getString("uuid", player.getUniqueId(), "pets_names", TABLE),
                                    new TypeToken<HashMap<String, String>>() {
                                    }.getType()));

                    plugin.getPetsManager().getPlayers().put(player.getName(), petsPlayer);

                    String lastActivePet = mySQl.getString("uuid", player.getUniqueId(), "active_pet", TABLE);
                    if (!lastActivePet.equalsIgnoreCase("null")) {
                        boolean blockedPet = mySQl.getBoolean("uuid", player.getUniqueId(), "blocked", TABLE);
                        String lastLocation = mySQl.getString("uuid", player.getUniqueId(), "last_location", TABLE);

                        Location spawnLocation;
                        if (plugin.getVariablesContainer().isSpawnToLastLocation()) {
                            if (lastLocation != null) spawnLocation = Strings.deserializeLocation(lastLocation);
                            else spawnLocation = player.getLocation();
                        } else {
                            spawnLocation = player.getLocation();
                        }

                        Bukkit.getScheduler().runTaskLater(plugin, () -> {
                            plugin.getPetsManager().spawnPet(lastActivePet, player, spawnLocation);
                            if (blockedPet)
                                plugin.getPetsManager().getPet(player).setPositionType(PetChangeSitEvent.PositionType.BLOCKED);
                        }, 1L);
                    }
                } else {
                    petsPlayer = new PetsPlayer(player.getUniqueId());
                    plugin.getPetsManager().getPlayers().put(player.getName(), petsPlayer);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, 1L);
    }

    @Override
    public void savePlayer(Player player) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> saveP(player), 1L);
    }

    private void saveP(Player player) {
        try {
            PetsPlayer petsPlayer = plugin.getPetsManager().getPlayers().get(player.getName());
            if (!mySQl.rowExists("uuid", player.getUniqueId().toString(), TABLE)) {
                mySQl.addRow(new String[]{
                        "uuid",
                        "active_pet",
                        "blocked",
                        "last_location",
                        "pets_names"
                }, new Object[]{
                        player.getUniqueId().toString(),
                        petsPlayer.getActivePetID(),
                        petsPlayer.getActivePet() == null ? 0 : petsPlayer.getActivePet().isBlocked(),
                        petsPlayer.getActivePet() == null ? "" : Strings.serializeLocation(petsPlayer.getActivePet().getHeadEntity().getLocation()),
                        GSON.toJson(petsPlayer.getPetNames())
                }, TABLE);
            } else {
                mySQl.set(new String[]{"active_pet", "blocked", "last_location", "pets_names"},
                        new Object[]{
                                petsPlayer.getActivePetID(),
                                petsPlayer.getActivePet() == null ? 0 : petsPlayer.getActivePet().isBlocked(),
                                petsPlayer.getActivePet() == null ? "" : Strings.serializeLocation(petsPlayer.getActivePet().getHeadEntity().getLocation()),
                                GSON.toJson(petsPlayer.getPetNames())},
                        "uuid", player.getUniqueId().toString(), TABLE);
            }
            plugin.getPetsManager().getPlayers().remove(player.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveAllPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            saveP(player);
        }
    }
}
