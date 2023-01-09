package me.imbuzz.dev.petsreloaded.nms.v1_17_R1.entities;

import me.imbuzz.dev.petsreloaded.core.objects.pets.PetEntity;
import me.imbuzz.dev.petsreloaded.core.objects.pets.settings.EntitySettings;
import me.imbuzz.dev.petsreloaded.nms.v1_17_R1.goals.PathFinderGoalPetsBasic;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.PathfinderGoalLookAtPlayer;
import net.minecraft.world.entity.ai.goal.PathfinderGoalSelector;
import net.minecraft.world.entity.animal.EntitySheep;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.level.World;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityTargetEvent;

import java.lang.reflect.Field;

public class NMSPetEntity extends EntitySheep {
    private final PetEntity pet;

    public NMSPetEntity(World world) {
        super(EntityTypes.ax, world);
        pet = null;
    }

    public NMSPetEntity(PetEntity petEntity, Entity owner, double speed) {
        super(EntityTypes.ax, ((CraftWorld) owner.getWorld()).getHandle());

        pet = petEntity;
        if (t == null) return;

        bP = new PathfinderGoalSelector(t.getMethodProfilerSupplier());
        bQ = new PathfinderGoalSelector(t.getMethodProfilerSupplier());

        bP.a(0, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        bP.a(1, new PathFinderGoalPetsBasic(petEntity, this, speed, (float) ((EntitySettings) petEntity.getPetSettings()).getDistanceInBlock(), 2.3F));

        setGoalTarget(((EntityLiving) ((CraftEntity) owner).getHandle()), EntityTargetEvent.TargetReason.CUSTOM, false);
    }

    @Override
    public void g(Vec3D vec3d) {
        if (isVehicle()) {
            EntityLiving entityliving = (EntityLiving) ((net.minecraft.world.entity.Entity) this).at.get(0);

            this.setYRot(entityliving.getYRot());
            this.x = this.getYRot();
            this.setXRot(entityliving.getXRot() * 0.5F);
            this.setYawPitch(this.getYRot(), this.getXRot());
            this.aX = this.getYRot();
            this.aZ = this.aX;

            double sideMot = entityliving.bo * ((float) pet.getSettings().getSpeed() / 3);
            double forMot = entityliving.bq * ((float) pet.getSettings().getSpeed() / 3);

            if (forMot <= 0.0F) {
                forMot *= 0.45F;
            }

            double y = vec3d.getY();

            if (this.z) {
                try {
                    Field jump = EntityLiving.class.getDeclaredField("bn");
                    jump.setAccessible(true);
                    if (jump.getBoolean(entityliving)) {
                        y = 0.5;
                        setMot(vec3d.getX(), y, vec3d.getZ());
                    }
                } catch (Exception ignored) {
                }
            }


            this.O = 1.0F;// The custom entity will now automatically climb up 1 high blocks

            vec3d = new Vec3D(sideMot, y, forMot);
        }

        super.g(vec3d);

    }

}
