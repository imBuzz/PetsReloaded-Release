package me.imbuzz.dev.petsreloaded.core.listeners;

import me.imbuzz.dev.petsreloaded.api.events.PetChangeSitEvent;
import me.imbuzz.dev.petsreloaded.core.PetsReloaded;
import me.imbuzz.dev.petsreloaded.core.managers.PetsManager;
import me.imbuzz.dev.petsreloaded.core.objects.pets.PetEntity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class WorldListener implements Listener {

    public static final String PET_CHUNK_METADATA = "PETSRELOADED_RESPAWN_PET_BY_CHUNK_SYSTEM";

    private final PetsManager petsManager = PetsReloaded.get().getPetsManager();

    public WorldListener() {
        Bukkit.getPluginManager().registerEvents(this, PetsReloaded.get());
    }

    @EventHandler
    public void chunkLoad(ChunkLoadEvent event) {
        for (Entity entity : event.getChunk().getEntities()) {
            if (!entity.hasMetadata(PET_CHUNK_METADATA)) continue;

            String[] metadata = entity.getMetadata(PET_CHUNK_METADATA).get(0).asString().split(":");
            entity.remove();

            Player owner = Bukkit.getPlayer(metadata[0]);
            if (owner == null) return;

            PetEntity petEntity = (PetEntity) petsManager.getPet(owner);
            if (petEntity == null) return;

            if (!petEntity.getPetSettings().getTag().equals(metadata[1])) return;

            petEntity.die();
            petEntity.spawn(petsManager, owner,
                    new Location(event.getWorld(), Double.parseDouble(metadata[2]), Double.parseDouble(metadata[3]), Double.parseDouble(metadata[4])),
                    PetsReloaded.get().getNmsHandler());

            petEntity.setPositionType(PetChangeSitEvent.PositionType.BLOCKED);
        }
    }

    @EventHandler
    public void chunkUnload(ChunkUnloadEvent event) {
        for (Entity entity : event.getChunk().getEntities()) {
            if (!petsManager.getActivePetsByEntities().containsKey(entity.getEntityId())) continue;
            if (petsManager.getActivePetsByEntities().get(entity.getEntityId()) == null) continue;
            if (!petsManager.getPlayers().containsKey(petsManager.getActivePetsByEntities().get(entity.getEntityId())))
                continue;

            PetEntity petEntity = petsManager.getPlayers().get(petsManager.getActivePetsByEntities().get(entity.getEntityId())).getActivePet();
            if (petEntity == null || petEntity.getHeadEntity().getEntityId() != entity.getEntityId()) continue;
            if (petEntity.getPositionType() != PetChangeSitEvent.PositionType.BLOCKED) continue;

            petEntity.fakeDie();

            petEntity.getHeadEntity().setMetadata(PET_CHUNK_METADATA,
                    new FixedMetadataValue(PetsReloaded.get(), petEntity.getOwner() + ":" +
                            petEntity.getPetSettings().getTag()

                            + ":" + petEntity.getHeadEntity().getLocation().getX()
                            + ":" + petEntity.getHeadEntity().getLocation().getY()
                            + ":" + petEntity.getHeadEntity().getLocation().getZ()));
        }
    }

}
