package me.imbuzz.dev.petsreloaded.core.objects.pets;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.imbuzz.dev.petsreloaded.api.events.PetChangeSitEvent;
import me.imbuzz.dev.petsreloaded.api.managers.IPetSettings;
import me.imbuzz.dev.petsreloaded.api.objects.IPetEntity;
import me.imbuzz.dev.petsreloaded.core.PetsReloaded;
import me.imbuzz.dev.petsreloaded.core.hook.ImplementedHookType;
import me.imbuzz.dev.petsreloaded.core.hook.hooks.ModelEngineHook;
import me.imbuzz.dev.petsreloaded.core.managers.PetsManager;
import me.imbuzz.dev.petsreloaded.core.nms.INMSHandler;
import me.imbuzz.dev.petsreloaded.core.objects.pets.enums.PetStatus;
import me.imbuzz.dev.petsreloaded.core.objects.pets.settings.EntitySettings;
import me.imbuzz.dev.petsreloaded.core.utils.Strings;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Map;

@RequiredArgsConstructor @Getter
public class PetEntity implements IPetEntity {

    private final EntitySettings settings;
    private final String owner;
    private final Map<String, ComponentEntity> entities = Maps.newHashMap();

    private Entity headEntity;
    @Setter private PetChangeSitEvent.PositionType positionType;

    public void spawn(PetsManager petsManager, Player owner, Location l, INMSHandler nmsHandler) {
        if (owner.getLocation().getWorld() == null) return;

        boolean wasEntityTypeNull = false;
        if (settings.getEntityType() == null) {
            wasEntityTypeNull = true;
            settings.setEntityType(EntityType.SHEEP);
        }

        headEntity = nmsHandler.getEntityHandler().spawnBase(this, owner, l);
        nmsHandler.getEntityHandler().updatePetStatus(headEntity, PetStatus.SILENT, PetStatus.INVULNERABLE);

        if (!wasEntityTypeNull) {
            if (settings.getEntityType() != EntityType.SHEEP) {
                nmsHandler.getEntityHandler().updatePetStatus(headEntity, PetStatus.INVISIBLE);
                Entity entity = owner.getWorld().spawnEntity(owner.getLocation(), settings.getEntityType());

                nmsHandler.getEntityHandler().updatePetStatus(entity, PetStatus.SILENT, PetStatus.INVULNERABLE, PetStatus.NO_AI);
                petsManager.getActivePetsByEntities().put(entity.getEntityId(), owner.getName());

                entities.put("main_entity", new ComponentEntity(entity, this));
            } else {
                entities.put("main_entity", new ComponentEntity(headEntity, this));
            }
        } else {
            nmsHandler.getEntityHandler().updatePetStatus(headEntity, PetStatus.INVISIBLE);
            settings.setEntityType(null);
        }

        for (EntitySettings.StructureSettings value : settings.getStructureSettings().values()) {
            Location location = headEntity.getLocation().clone();
            Vector direction = getDirection(location);

            Location newLocation = location.add(direction.multiply(value.xOffset));
            float zvp = (float) (newLocation.getZ() + value.zOffset * Math.sin(Math.toRadians(newLocation.getYaw())));
            float xvp = (float) (newLocation.getX() + value.zOffset * Math.cos(Math.toRadians(newLocation.getYaw())));

            Location loc = new Location(headEntity.getWorld(), xvp, headEntity.getLocation().getY() + value.yOffset, zvp, newLocation.getYaw(), 0);

            ArmorStand stand = (ArmorStand) owner.getLocation().getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
            nmsHandler.getEntityHandler().updatePetStatus(stand, PetStatus.SILENT, PetStatus.INVULNERABLE);

            if (!value.displayName.isEmpty() && value.tag.equalsIgnoreCase("pet_display_name")) {
                String customPetName = petsManager.getPlayers().get(owner.getName()).getPetNames()
                        .getOrDefault(settings.getTag(), value.displayName.replace("%owner%", owner.getName()))
                        .replace("_", " ");

                Entity nameEntity = entities.containsKey("main_entity") ? entities.get("main_entity").getEntity() : stand;

                nameEntity.setCustomNameVisible(true);
                nameEntity.setCustomName(Strings.colorize(customPetName));
            }

            stand.setArms(true);
            stand.setGravity(false);

            stand.setBasePlate(value.plate);
            stand.setVisible(value.visibile);
            stand.setSmall(value.small);

            value.equip(stand, settings);
            value.pose(stand);

            if (value.modelName != null && !value.modelName.isEmpty()) {
                if (PetsReloaded.get().isHookEnabled(ImplementedHookType.MODEL_ENGINE)) {
                    ((ModelEngineHook) PetsReloaded.get().getHook(ImplementedHookType.MODEL_ENGINE)).
                            disguise(stand, value.modelName, value.visibile ? PetStatus.EMPTY : PetStatus.INVISIBLE);
                }
            }

            ComponentEntity componentEntity = new ComponentEntity(stand, this);

            componentEntity.setDistanceX(value.xOffset);
            componentEntity.setDistanceY(value.yOffset);
            componentEntity.setDistanceZ(value.zOffset);

            petsManager.getActivePetsByEntities().put(stand.getEntityId(), owner.getName());
            entities.put(value.tag, componentEntity);
        }

        if (settings.getModelName() != null && !settings.getModelName().isEmpty()) {
            if (PetsReloaded.get().isHookEnabled(ImplementedHookType.MODEL_ENGINE)) {
                ((ModelEngineHook) PetsReloaded.get().getHook(ImplementedHookType.MODEL_ENGINE)).disguise(headEntity, settings.getModelName(),
                        wasEntityTypeNull ? PetStatus.INVISIBLE : PetStatus.EMPTY);
            }
        }

        positionType = PetChangeSitEvent.PositionType.UNBLOCKED;
    }

    private Vector getDirection(Location location) {
        Vector vector = new Vector();
        vector.setX(-Math.sin(Math.toRadians(location.getYaw())));
        vector.setZ(Math.cos(Math.toRadians(location.getYaw())));
        return vector;
    }

    public void move() {
        for (ComponentEntity entity : entities.values()) entity.move();
    }

    public void die() {
        die(false);
    }

    public void die(boolean teleport) {
        if (teleport && PetsReloaded.get().isHookEnabled(ImplementedHookType.MODEL_ENGINE)) {
            ((ModelEngineHook) (PetsReloaded.get().getHook(ImplementedHookType.MODEL_ENGINE)))
                    .undisguise(this);
        }

        headEntity.remove();
        for (ComponentEntity entity : entities.values()) {
            entity.die();
        }
    }

    public void fakeDie() {
        for (ComponentEntity value : entities.values()) {
            value.getEntity().remove();
        }
        entities.clear();
    }

    public void tick(PetsManager petsManager, INMSHandler inmsHandler) {
        Player player = Bukkit.getPlayer(owner);
        if (player == null) return;
        if (headEntity == null) return;

        if (shouldTeleport(player)) {
            if (PetsReloaded.get().getVariablesContainer().getDisabledWorlds().contains(player.getWorld().getName())) {
                player.sendMessage(Strings.colorize(PetsReloaded.get().getVariablesContainer().getTeleport_to_disabled_world()
                        .replace("%pet%", settings.getPetName()))
                        .replace("%player%", player.getName()));
                petsManager.despawnPet(this);
                return;
            } else {
                teleport(petsManager, player, inmsHandler);
            }
        } else if (headEntity.getLocation().distance(player.getLocation()) >= 10 && !player.isFlying()) {
            if (positionType != PetChangeSitEvent.PositionType.BLOCKED)
                teleport(petsManager, player, inmsHandler);
            else return;
        }

        move();
    }

    @Override
    public IPetSettings getPetSettings() {
        return settings;
    }

    @Override
    public boolean isBlocked() {
        return positionType == PetChangeSitEvent.PositionType.BLOCKED;
    }

    @Override
    public void enableName(boolean state) {
        if (!entities.containsKey("pet_display_name")) return;
        ArmorStand stand = (ArmorStand) entities.get("pet_display_name").getEntity();
        if (stand.getCustomName() != null && !stand.getCustomName().isEmpty()) stand.setCustomNameVisible(state);
    }

    private boolean shouldTeleport(Player player) {
        return headEntity.getWorld() != player.getWorld();
    }

    private void teleport(PetsManager petsManager, Player player, INMSHandler inmsHandler) {
        die(true);
        spawn(petsManager, player, player.getLocation(), inmsHandler);
    }

}
