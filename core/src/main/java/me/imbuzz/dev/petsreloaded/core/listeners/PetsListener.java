package me.imbuzz.dev.petsreloaded.core.listeners;

import me.imbuzz.dev.petsreloaded.api.objects.IPetEntity;
import me.imbuzz.dev.petsreloaded.core.PetsReloaded;
import me.imbuzz.dev.petsreloaded.core.files.SettingsFile;
import me.imbuzz.dev.petsreloaded.core.managers.PetsManager;
import me.imbuzz.dev.petsreloaded.core.objects.pets.PetEntity;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

public class PetsListener implements Listener {
    private final PetsReloaded petsReloaded;
    private final PetsManager petsManager;

    public PetsListener(PetsReloaded petsReloaded, PetsManager petsManager) {
        this.petsReloaded = petsReloaded;
        this.petsManager = petsManager;
        Bukkit.getPluginManager().registerEvents(this, petsReloaded);
    }

    @EventHandler
    public void entityTarget(EntityTargetEvent event) {
        Entity target = event.getTarget();
        if (target == null) return;
        if (target.hasMetadata("PET")) event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        petsReloaded.getDataLoader().loadPlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        petsReloaded.getDataLoader().savePlayer(event.getPlayer());

        if (!petsManager.hasActivePet(event.getPlayer())) return;
        petsManager.despawnPet(petsManager.getPet(event.getPlayer()), false);
    }

    @EventHandler
    public void entityDamage(EntityDamageEvent event) {
        if (event.getEntity().hasMetadata("PET")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void tameEvent(EntityTameEvent event) {
        if (event.getEntity().hasMetadata("PET")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void gamemodeChange(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        if (!petsManager.getPlayers().containsKey(player.getName())) return;
        if (!(petsManager.hasActivePet(player) && event.getNewGameMode() == GameMode.SPECTATOR)) return;

        IPetEntity petEntity = petsManager.getPet(player);
        petsManager.despawnPet(petEntity);
    }

    @EventHandler
    public void onInteract(EntityInteractEvent event) {
        if (!event.getEntity().hasMetadata("PET")) return;
        if (petsReloaded.getVariablesContainer().isCanInteract()) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void interactEntity(PlayerInteractEntityEvent event) {
        Entity clickedEntity = event.getRightClicked();
        if (!clickedEntity.hasMetadata("PET")) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void interactAtEntity(PlayerInteractAtEntityEvent event) {
        Entity clickedEntity = event.getRightClicked();
        if (clickedEntity == null) {
            return;
        }
        if (!clickedEntity.hasMetadata("PET")) {
            return;
        }

        event.setCancelled(true);

        if (!petsReloaded.getSettings().getProperty(SettingsFile.CAN_RIDE)) {
            return;
        }
        if (!petsManager.getActivePetsByEntities().containsKey(clickedEntity.getEntityId())) {
            return;
        }

        PetEntity petEntity = petsManager.getPlayers().get(petsManager.getActivePetsByEntities().get(clickedEntity.getEntityId())).getActivePet();

        if (petEntity == null) {
            return;
        }
        if (!petEntity.getOwner().equalsIgnoreCase(event.getPlayer().getName())) {
            return;
        }
        if (!petEntity.getSettings().isMountable()) {
            return;
        }

        petEntity.getHeadEntity().setPassenger(event.getPlayer());

        if (!petsReloaded.getVariablesContainer().isEnableRidingName()) petEntity.enableName(false);
    }

    @EventHandler
    public void leavePet(EntityDismountEvent event) {
        if (event.getEntity().getType() != EntityType.PLAYER) return;
        if (!event.getDismounted().hasMetadata("PET")) return;
        if (!petsManager.getActivePetsByEntities().containsKey(event.getDismounted().getEntityId())) return;

        PetEntity petEntity = petsManager.getPlayers().get(petsManager.getActivePetsByEntities().get(event.getDismounted().getEntityId())).getActivePet();
        if (petEntity == null) return;

        if (!petsReloaded.getVariablesContainer().isEnableRidingName()) petEntity.enableName(true);
    }

}
