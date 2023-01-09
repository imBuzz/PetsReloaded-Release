package me.imbuzz.dev.petsreloaded.nms.v1_9_R1.entities;

import me.imbuzz.dev.petsreloaded.core.objects.pets.PetEntity;
import me.imbuzz.dev.petsreloaded.core.objects.pets.settings.EntitySettings;
import me.imbuzz.dev.petsreloaded.nms.v1_9_R1.goals.PathFinderGoalPetsBasic;
import net.minecraft.server.v1_9_R1.*;
import org.bukkit.craftbukkit.v1_9_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftEntity;
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
        if (!passengers.isEmpty()) {
            net.minecraft.server.v1_9_R1.Entity passenger = passengers.get(0);

            this.lastYaw = this.yaw = passenger.yaw;
            this.pitch = (passenger.pitch * 0.5F);
            setYawPitch(this.yaw, this.pitch);
            this.aO = this.aM = this.yaw;

            sideMot = (((EntityLiving) passenger).bd * ((float) pet.getSettings().getSpeed() / 7));
            forMot = ((EntityLiving) passenger).be * ((float) pet.getSettings().getSpeed() / 7);
            if (forMot <= 0.0F) {
                forMot *= 0.25F;// Make backwards slower
            }

            if (this.onGround) {
                try {
                    Field jump = EntityLiving.class.getDeclaredField("bc");
                    jump.setAccessible(true);
                    if (jump.getBoolean(passenger)) this.motY = 0.5D;
                } catch (Exception ignored) {
                }
            }

            this.P = 1.0F;// The custom entity will now automatically climb up 1 high blocks
            this.aQ = this.ck() * 0.1F;
            if (!this.world.isClientSide) {
                //this.j(0.35F);
                super.g(sideMot, forMot);
            }


            this.aE = this.aF;//Some extra things
            double d0 = this.locX - this.lastX;
            double d1 = this.locZ - this.lastZ;
            float f4 = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0F;
            if (f4 > 1.0F) {
                f4 = 1.0F;
            }

            this.aF += (f4 - this.aF) * 0.4F;
            this.aG += this.aF;

        }

        super.g(sideMot, forMot);


    }


}
