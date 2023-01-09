package me.imbuzz.dev.petsreloaded.core.managers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import lombok.Getter;
import me.imbuzz.dev.petsreloaded.api.events.PetDespawnEvent;
import me.imbuzz.dev.petsreloaded.api.events.PetSpawnEvent;
import me.imbuzz.dev.petsreloaded.api.managers.IPetsManager;
import me.imbuzz.dev.petsreloaded.api.objects.IPetEntity;
import me.imbuzz.dev.petsreloaded.core.PetsReloaded;
import me.imbuzz.dev.petsreloaded.core.files.files.PetFile;
import me.imbuzz.dev.petsreloaded.core.listeners.PetsListener;
import me.imbuzz.dev.petsreloaded.core.objects.pets.ComponentEntity;
import me.imbuzz.dev.petsreloaded.core.objects.pets.PetEntity;
import me.imbuzz.dev.petsreloaded.core.objects.pets.settings.EntitySettings;
import me.imbuzz.dev.petsreloaded.core.objects.player.PetsPlayer;
import me.imbuzz.dev.petsreloaded.core.utils.Strings;
import me.imbuzz.dev.petsreloaded.core.workload.WorkLoadThread;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class PetsManager implements IPetsManager {

    public static final ImmutableList<String> DEFAULT_PETS =
            ImmutableList.of("TurtlePet", "MonkeyPet", "DogePugPet", "MagicPugPet", "WhitePugPet",
                    "PenguinPet", "FrogPet", "GorillaPet", "FashionGorillaPet", "PigPet");

    @Getter
    private final Map<String, EntitySettings> petsPresets = Maps.newConcurrentMap();


    //NAME - OBJECT
    @Getter
    private final Map<String, PetsPlayer> players = Maps.newConcurrentMap();
    //ENTITY ID - PET OWNER
    @Getter
    private final Map<Integer, String> activePetsByEntities = Maps.newConcurrentMap();

    @Getter
    private boolean loadedPets;

    private final PetsReloaded petsPlugin;
    private final WorkLoadThread workLoadThread;

    public PetsManager(PetsReloaded pl) {
        this.petsPlugin = pl;
        loadPets();

        workLoadThread = new WorkLoadThread(petsPlugin);
        workLoadThread.runTaskTimer(petsPlugin, 50L, 1L);
        Bukkit.getScheduler().runTaskTimer(petsPlugin, this::checkForPetsToTeleport, 100L, 1L);

        loadedPets = true;

        new PetsListener(petsPlugin, this);
    }

    @Override
    public boolean hasActivePet(Player player) {
        return getPet(player) != null;
    }

    @Override
    public IPetEntity getPet(Player player) {
        return players.get(player.getName()).getActivePet();
    }

    @Override
    public String[] despawnPet(Player player) {
        return despawnPet(players.get(player.getName()).getActivePet());
    }

    @Override
    public String[] despawnPet(IPetEntity pet) {
        return despawnPet(pet, true);
    }

    public String[] despawnPet(IPetEntity pet, boolean removeID) {
        PetDespawnEvent despawnEvent = new PetDespawnEvent(pet);
        Bukkit.getPluginManager().callEvent(despawnEvent);

        if (!despawnEvent.isCancelled()) {
            PetEntity petEntity = (PetEntity) pet;
            for (ComponentEntity entity : petEntity.getEntities().values())
                activePetsByEntities.remove(entity.getEntity().getEntityId());

            petEntity.die();
            if (removeID) players.get(pet.getOwner()).setActivePet(null);
            return new String[]{petEntity.getPetSettings().getTag(), petEntity.getSettings().getPetName()};
        }

        return new String[]{};
    }

    @Override
    public void spawnPet(String petTag, Player player) {
        spawnPet(petTag, player, player.getLocation());
    }

    public void spawnPet(String petTag, Player player, Location location) {
        if (!petsPresets.containsKey(petTag)) {
            petsPlugin.getLogger().severe("Pet with tag: " + petTag + " is not present!");
            return;
        }

        if (hasActivePet(player)) {
            petsPlugin.getLogger().severe("You have to remove the active pet on a player before trying to spawn another one!");
            return;
        }

        EntitySettings entitySettings = petsPresets.get(petTag);
        PetEntity petEntity = new PetEntity(entitySettings, player.getName());

        PetSpawnEvent spawnEvent = new PetSpawnEvent(petEntity);
        Bukkit.getPluginManager().callEvent(spawnEvent);

        if (!spawnEvent.isCancelled()) {
            players.get(player.getName()).setActivePet(petEntity);

            petEntity.spawn(this, player, location, petsPlugin.getNmsHandler());
            petEntity.move();
        }
    }

    public Collection<PetEntity> getActivePets() {
        return players.values().stream().map(PetsPlayer::getActivePet)
                .filter(Objects::nonNull).collect(Collectors.toList());
    }

    private void loadPets() {
        File petFolder = new File(petsPlugin.getDataFolder().getPath() + File.separator + "pets");
        if (!petFolder.exists()) {
            petFolder.mkdirs();

            for (String basicPet : DEFAULT_PETS) {
                File targetFile = new File(petFolder.getPath() + File.separator + basicPet + ".yml");
                if (!targetFile.exists()) {
                    try {
                        InputStream inputStream = petsPlugin.getResource("pets/" + basicPet + ".yml");
                        byte[] buffer = new byte[inputStream.available()];
                        inputStream.read(buffer);
                        targetFile.getParentFile().mkdirs();
                        OutputStream outStream = new FileOutputStream(targetFile);
                        outStream.write(buffer);
                        inputStream.close();
                        outStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        File[] files = petFolder.listFiles();
        if (files == null) return;

        int loadedPets = 0;

        for (File file : files) {
            PetFile petFile = new PetFile(file);
            if (petFile.load(petsPlugin) == null) continue;

            petsPresets.put(ChatColor.stripColor(petFile.getEntitySettings().getTag()), petFile.getEntitySettings());
            loadedPets++;
        }

        Bukkit.getConsoleSender().sendMessage(Strings.colorize("[PetsReloaded]" + ChatColor.GREEN + " Loaded " + loadedPets + " pets successfully!"));
    }

    private void loadPlayers() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            petsPlugin.getDataLoader().loadPlayer(onlinePlayer);
        }
    }

    public void stop() {
        loadedPets = false;
        for (PetEntity value : getActivePets()) {
            despawnPet(value, false);
        }
        petsPlugin.getDataLoader().saveAllPlayers();

        players.clear();
        petsPresets.clear();
    }

    public void reload() {
        stop();
        loadPets();
        loadPlayers();

        loadedPets = true;
    }

    private void checkForPetsToTeleport() {
        for (PetEntity value : getActivePets()) {
            if (value == null) return;
            workLoadThread.addLoad(() -> value.tick(this, petsPlugin.getNmsHandler()));
        }
    }


}
