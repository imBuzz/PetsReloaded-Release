package me.imbuzz.dev.petsreloaded.nms.v1_8_R1.entities;

import me.imbuzz.dev.petsreloaded.core.objects.pets.PetEntity;
import me.imbuzz.dev.petsreloaded.core.objects.pets.settings.EntitySettings;
import me.imbuzz.dev.petsreloaded.nms.v1_8_R1.goals.PathFinderGoalPetsBasic;
import net.minecraft.server.v1_8_R1.*;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftEntity;
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
    public void g(float sideMot, float forMot) {
        if (passenger != null) {
            this.lastYaw = this.yaw = this.passenger.yaw;
            this.pitch = (passenger.pitch * 0.5F);
            setYawPitch(this.yaw, this.pitch);
            this.aI = this.aG = this.yaw;

            sideMot = (((EntityLiving) passenger).aX * ((float) pet.getSettings().getSpeed() / 7));
            forMot = ((EntityLiving) passenger).aY * ((float) pet.getSettings().getSpeed() / 7);
            if (forMot <= 0.0F) {
                forMot *= 0.25F;// Make backwards slower
            }

            if (this.onGround) {
                try {
                    Field jump = EntityLiving.class.getDeclaredField("aW");
                    jump.setAccessible(true);
                    if (jump.getBoolean(passenger)) this.motY = 0.5D;
                } catch (Exception ignored) {
                }
            }

            this.S = 1.0F;// The custom entity will now automatically climb up 1 high blocks
            this.aK = this.bH() * 0.1F;
            if (!this.world.isStatic) {
                this.j(0.35F);//Here is the speed the entity will walk.
                super.g(sideMot, forMot);
            }


            this.ay = this.az;//Some extra things
            double d0 = this.locX - this.lastX;
            double d1 = this.locZ - this.lastZ;
            float f4 = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0F;
            if (f4 > 1.0F) {
                f4 = 1.0F;
            }

            this.az += (f4 - this.az) * 0.4F;
            this.aA += this.az;

        }

        super.g(sideMot, forMot);


    }


}
