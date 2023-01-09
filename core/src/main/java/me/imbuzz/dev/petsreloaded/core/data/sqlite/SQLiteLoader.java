package me.imbuzz.dev.petsreloaded.core.data.sqlite;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.imbuzz.dev.petsreloaded.api.events.PetChangeSitEvent;
import me.imbuzz.dev.petsreloaded.core.PetsReloaded;
import me.imbuzz.dev.petsreloaded.core.data.DataLoader;
import me.imbuzz.dev.petsreloaded.core.data.LoaderType;
import me.imbuzz.dev.petsreloaded.core.objects.player.PetsPlayer;
import me.imbuzz.dev.petsreloaded.core.utils.Strings;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

public class SQLiteLoader implements DataLoader {

    private static final Gson GSON = new Gson();
    private final String TABLE = "pets_data";
    private final PetsReloaded plugin = PetsReloaded.get();
    private Connection connection;

    @Override
    public boolean init() {
        try {
            File databaseFile = new File(plugin.getDataFolder(), "database.db");
            if (!databaseFile.exists()) {
                databaseFile.createNewFile();
            }

            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFile.getAbsolutePath());
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(5);

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + TABLE +
                    " (uuid VARCHAR(36) PRIMARY KEY, active_pet VARCHAR(25), blocked VARCHAR(4), last_location VARCHAR(3000), pets_names VARCHAR(8000))");

            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public LoaderType getType() {
        return LoaderType.SQLITE;
    }

    @Override
    public void loadPlayer(Player player) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            try {
                PetsPlayer petsPlayer;
                if (isPlayerPresentOnDB(player)) {
                    petsPlayer = new PetsPlayer(player.getUniqueId(),
                            GSON.fromJson(getString(player, "pets_names"),
                                    new TypeToken<HashMap<String, String>>() {
                                    }.getType()));

                    plugin.getPetsManager().getPlayers().put(player.getName(), petsPlayer);

                    String lastActivePet = getString(player, "active_pet");
                    if (lastActivePet != null && !lastActivePet.equalsIgnoreCase("null")) {
                        String blockedPet = getString(player, "blocked");
                        String lastLocation = getString(player, "last_location");

                        Location spawnLocation;
                        if (plugin.getVariablesContainer().isSpawnToLastLocation()) {
                            if (lastLocation != null) spawnLocation = Strings.deserializeLocation(lastLocation);
                            else spawnLocation = player.getLocation();
                        } else {
                            spawnLocation = player.getLocation();
                        }

                        Bukkit.getScheduler().runTaskLater(plugin, () -> {
                            plugin.getPetsManager().spawnPet(lastActivePet, player, spawnLocation);
                            if (blockedPet != null && blockedPet.equalsIgnoreCase("true")) {
                                plugin.getPetsManager().getPet(player).setPositionType(PetChangeSitEvent.PositionType.BLOCKED);
                            }
                        }, 1L);
                    }
                } else {
                    petsPlayer = new PetsPlayer(player.getUniqueId());
                    plugin.getPetsManager().getPlayers().put(player.getName(), petsPlayer);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 1L);
    }

    @Override
    public void savePlayer(Player player) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> saveP(player), 1L);
    }

    private void saveP(Player player) {
        PetsPlayer petsPlayer = plugin.getPetsManager().getPlayers().get(player.getName());
        if (!isPlayerPresentOnDB(player)) {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("INSERT INTO " + TABLE + " (uuid, active_pet, blocked, last_location, pets_names) " + "VALUES ('" +
                        player.getUniqueId().toString() + "', '" + petsPlayer.getActivePetID() + "', '"
                        + (petsPlayer.getActivePet() != null && petsPlayer.getActivePet().isBlocked()) + "', '"
                        + (petsPlayer.getActivePet() == null ? null : Strings.serializeLocation(petsPlayer.getActivePet().getHeadEntity().getLocation())) + "', '"
                        + GSON.toJson(petsPlayer.getPetNames()) + "')");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("UPDATE " + TABLE + " SET active_pet='" + petsPlayer.getActivePetID() + "', "
                        + "blocked='" + (petsPlayer.getActivePet() != null && petsPlayer.getActivePet().isBlocked()) + "', "
                        + "last_location='" + (petsPlayer.getActivePet() == null ? "" : Strings.serializeLocation(petsPlayer.getActivePet().getHeadEntity().getLocation())) + "', "
                        + "pets_names='" + GSON.toJson(petsPlayer.getPetNames()) + "' WHERE uuid='" + player.getUniqueId().toString() + "'");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        plugin.getPetsManager().getPlayers().remove(player.getName());
    }

    @Override
    public void saveAllPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            saveP(player);
        }
    }

    private boolean isPlayerPresentOnDB(Player player) {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE + " WHERE uuid='" + player.getUniqueId().toString() + "'");
            boolean isPresent = resultSet.next();

            resultSet.close();
            statement.close();

            return isPresent;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getString(Player player, String column) {
        try (Statement statement = connection.createStatement()) {
            ResultSet set = statement.executeQuery("SELECT * FROM " + TABLE + " WHERE uuid='" + player.getUniqueId().toString() + "' LIMIT 1");
            set.next();
            String value = set.getString(column);

            set.close();
            statement.close();

            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
