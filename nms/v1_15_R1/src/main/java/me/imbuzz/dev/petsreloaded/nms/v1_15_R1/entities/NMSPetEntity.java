package me.imbuzz.dev.petsreloaded.nms.v1_15_R1.entities;

import me.imbuzz.dev.petsreloaded.core.objects.pets.PetEntity;
import me.imbuzz.dev.petsreloaded.core.objects.pets.settings.EntitySettings;
import me.imbuzz.dev.petsreloaded.nms.v1_15_R1.goals.PathFinderGoalPetsBasic;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityTargetEvent;

import java.lang.reflect.Field;

public class NMSPetEntity extends EntitySheep {
    private final PetEntity pet;

    public NMSPetEntity(World world) {
        super(EntityTypes.SHEEP, world);
        pet = null;
    }

    public NMSPetEntity(PetEntity petEntity, Entity owner, double speed) {
        super(EntityTypes.SHEEP, ((CraftWorld) owner.getWorld()).getHandle());

        pet = petEntity;
        GameProfilerFiller gameProfilerFiller = world != null && world.getMethodProfiler() != null ? world.getMethodProfiler() : null;

        goalSelector = new PathfinderGoalSelector(gameProfilerFiller);
        targetSelector = new PathfinderGoalSelector(gameProfilerFiller);

        goalSelector.a(0, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        goalSelector.a(1, new PathFinderGoalPetsBasic(petEntity, this, speed, (float) ((EntitySettings) petEntity.getPetSettings()).getDistanceInBlock(), 2.3F));

        setGoalTarget(((EntityLiving) ((CraftEntity) owner).getHandle()), EntityTargetEvent.TargetReason.CUSTOM, false);
    }

    @Override
    public void e(Vec3D vec3d) {
        if (!passengers.isEmpty()) {
            EntityLiving entityliving = (EntityLiving) passengers.get(0);
            this.yaw = entityliving.yaw;
            this.lastYaw = this.yaw;
            this.pitch = entityliving.pitch * 0.5F;
            this.setYawPitch(this.yaw, this.pitch);

            this.aI = this.yaw;
            this.aK = this.aI;

            double sideMot = entityliving.aZ * ((float) pet.getSettings().getSpeed() / 3);
            double forMot = entityliving.bb * ((float) pet.getSettings().getSpeed() / 3);

            if (forMot <= 0.0F) {
                forMot *= 0.45F;
            }

            double y = vec3d.getY();

            if (this.onGround) {
                try {
                    Field jump = EntityLiving.class.getDeclaredField("jumping");
                    jump.setAccessible(true);
                    if (jump.getBoolean(entityliving)) {
                        y = 0.5;
                        setMot(vec3d.x, y, vec3d.z);
                    }
                } catch (Exception ignored) {
                }
            }

            this.H = 1.0F;// The custom entity will now automatically climb up 1 high blocks

            vec3d = new Vec3D(sideMot, y, forMot);
        }

        super.e(vec3d);

    }

}
