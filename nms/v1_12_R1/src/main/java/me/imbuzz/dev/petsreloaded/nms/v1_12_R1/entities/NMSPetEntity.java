package me.imbuzz.dev.petsreloaded.nms.v1_12_R1.entities;

import me.imbuzz.dev.petsreloaded.core.objects.pets.PetEntity;
import me.imbuzz.dev.petsreloaded.core.objects.pets.settings.EntitySettings;
import me.imbuzz.dev.petsreloaded.nms.v1_12_R1.goals.PathFinderGoalPetsBasic;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityTargetEvent;

import java.lang.reflect.Field;

public class NMSPetEntity extends EntitySheep {
    private final PetEntity pet;

    public NMSPetEntity(World world) {
        super(world);
        pet = null;
    }

    public NMSPetEntity(PetEntity petEntity, Entity owner, double speed) {
        super(((CraftWorld) owner.getWorld()).getHandle());

        pet = petEntity;
        MethodProfiler methodprofiler = world != null && world.methodProfiler != null ? world.methodProfiler : null;

        goalSelector = new PathfinderGoalSelector(methodprofiler);
        targetSelector = new PathfinderGoalSelector(methodprofiler);

        goalSelector.a(0, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        goalSelector.a(1, new PathFinderGoalPetsBasic(petEntity, this, speed, (float) ((EntitySettings) petEntity.getPetSettings()).getDistanceInBlock(), 2.3F));

        setGoalTarget(((EntityLiving) ((CraftEntity) owner).getHandle()), EntityTargetEvent.TargetReason.CUSTOM, false);
    }


    @Override
    public void a(float sideMot, float a, float forMot) {

        if (!passengers.isEmpty()) {

            EntityLiving entityliving = (EntityLiving) passengers.get(0);
            this.yaw = entityliving.yaw;
            this.lastYaw = this.yaw;
            this.pitch = entityliving.pitch * 0.5F;
            this.setYawPitch(this.yaw, this.pitch);
            this.aN = this.yaw;
            this.aP = this.aN;
            sideMot = entityliving.be * ((float) pet.getSettings().getSpeed() / 2);
            forMot = entityliving.bg * ((float) pet.getSettings().getSpeed() / 2);
            if (forMot <= 0.0F) {
                forMot *= 0.25F;
            }

            if (this.onGround) {
                try {
                    Field jump = EntityLiving.class.getDeclaredField("bd");
                    jump.setAccessible(true);
                    if (jump.getBoolean(entityliving)) this.motY += 0.5D;
                } catch (Exception ignored) {
                }
            }

            this.P = 1.0F;// The custom entity will now automatically climb up 1 high blocks

            /*this.aF = this.aG;
            double d0 = this.locX - this.lastX;
            double d1 = this.locZ - this.lastZ;
            float f5 = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0F;
            if (f5 > 1.0F) {
                f5 = 1.0F;
            }

            this.aG += (f5 - this.aG) * 0.4F;
            this.aH += this.aG;*/
        }

        super.a(sideMot, a, forMot);
    }

}
