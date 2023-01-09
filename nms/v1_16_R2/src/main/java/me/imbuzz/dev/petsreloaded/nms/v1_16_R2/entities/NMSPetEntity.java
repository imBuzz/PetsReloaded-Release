package me.imbuzz.dev.petsreloaded.nms.v1_16_R2.entities;

import me.imbuzz.dev.petsreloaded.core.objects.pets.PetEntity;
import me.imbuzz.dev.petsreloaded.core.objects.pets.settings.EntitySettings;
import me.imbuzz.dev.petsreloaded.nms.v1_16_R2.goals.PathFinderGoalPetsBasic;
import net.minecraft.server.v1_16_R2.*;
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftEntity;
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
        if (world == null) return;

        goalSelector = new PathfinderGoalSelector(world.getMethodProfilerSupplier());
        targetSelector = new PathfinderGoalSelector(world.getMethodProfilerSupplier());

        goalSelector.a(0, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        goalSelector.a(1, new PathFinderGoalPetsBasic(petEntity, this, speed, (float) ((EntitySettings) petEntity.getPetSettings()).getDistanceInBlock(), 2.3F));

        setGoalTarget(((EntityLiving) ((CraftEntity) owner).getHandle()), EntityTargetEvent.TargetReason.CUSTOM, false);
    }

    @Override
    public void g(Vec3D vec3d) {
        if (!passengers.isEmpty()) {
            EntityLiving entityliving = (EntityLiving) passengers.get(0);
            this.yaw = entityliving.yaw;
            this.lastYaw = this.yaw;
            this.pitch = entityliving.pitch * 0.5F;
            this.setYawPitch(this.yaw, this.pitch);

            this.aA = this.yaw;
            this.aC = this.aA;

            double sideMot = entityliving.aR * ((float) pet.getSettings().getSpeed() / 3);
            double forMot = entityliving.aT * ((float) pet.getSettings().getSpeed() / 3);

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

            this.G = 1.0F;// The custom entity will now automatically climb up 1 high blocks

            vec3d = new Vec3D(sideMot, y, forMot);
        }

        super.g(vec3d);

    }

}
