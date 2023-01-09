package me.imbuzz.dev.petsreloaded.nms.v1_15_R1;

import lombok.RequiredArgsConstructor;
import me.imbuzz.dev.petsreloaded.core.nms.interfacing.IEntityHandler;
import me.imbuzz.dev.petsreloaded.core.objects.pets.PetEntity;
import me.imbuzz.dev.petsreloaded.core.objects.pets.enums.PetStatus;
import me.imbuzz.dev.petsreloaded.nms.v1_15_R1.entities.NMSPetEntity;
import net.minecraft.server.v1_15_R1.EntityHuman;
import net.minecraft.server.v1_15_R1.EntityInsentient;
import net.minecraft.server.v1_15_R1.GameProfilerFiller;
import net.minecraft.server.v1_15_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_15_R1.PathfinderGoalSelector;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@RequiredArgsConstructor
public class EntityHandler implements IEntityHandler {
    private final NMSHandler nmsHandler;

    @Override
    public Entity spawnBase(PetEntity petEntity, Entity owner, Location location) {
        NMSPetEntity nmsPetEntity = new NMSPetEntity(petEntity, owner, petEntity.getSettings().getSpeed());
        nmsPetEntity.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        ((CraftLivingEntity) nmsPetEntity.getBukkitEntity()).setRemoveWhenFarAway(false);
        ((CraftWorld) owner.getWorld()).getHandle().addEntity(nmsPetEntity, CreatureSpawnEvent.SpawnReason.CUSTOM);

        nmsHandler.getPlugin().getPetsManager().getActivePetsByEntities().put(nmsPetEntity.getId(), owner.getName());
        return nmsPetEntity.getBukkitEntity();
    }

    @Override
    public void updatePetStatus(Entity entity, PetStatus... petStatuses) {
        net.minecraft.server.v1_15_R1.Entity nmsEntity = ((CraftEntity) entity).getHandle();

        for (PetStatus petStatus : petStatuses) {
            switch (petStatus) {
                case NO_AI: {
                    GameProfilerFiller gameProfilerFiller = nmsEntity.world != null && nmsEntity.world.getMethodProfiler() != null ?
                            nmsEntity.world.getMethodProfiler() : null;

                    ((EntityInsentient) nmsEntity).goalSelector = new PathfinderGoalSelector(gameProfilerFiller);
                    ((EntityInsentient) nmsEntity).goalSelector
                            .a(100, new PathfinderGoalLookAtPlayer((EntityInsentient) nmsEntity, EntityHuman.class, 8.0F));

                    break;
                }
                case SILENT: {
                    entity.setSilent(true);
                    break;
                }
                case INVULNERABLE: {
                    entity.setMetadata("PET", new FixedMetadataValue(nmsHandler.getPlugin(), true));
                    break;
                }
                case INVISIBLE: {
                    if (entity instanceof LivingEntity) ((LivingEntity) entity)
                            .addPotionEffect(
                                    new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false));
                    break;
                }
            }
        }
    }
}
