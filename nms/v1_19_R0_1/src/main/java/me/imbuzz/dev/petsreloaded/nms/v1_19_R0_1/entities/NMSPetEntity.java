package me.imbuzz.dev.petsreloaded.nms.v1_19_R0_1.entities;

import me.imbuzz.dev.petsreloaded.core.objects.pets.PetEntity;
import me.imbuzz.dev.petsreloaded.core.objects.pets.settings.EntitySettings;
import me.imbuzz.dev.petsreloaded.nms.v1_19_R0_1.goals.PathFinderGoalPetsBasic;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityTargetEvent;

import java.lang.reflect.Field;

public class NMSPetEntity extends Sheep {
    private final PetEntity pet;

    public NMSPetEntity(Level world) {
        super(EntityType.SHEEP, world);
        pet = null;
    }

    public NMSPetEntity(PetEntity petEntity, Entity owner, double speed) {
        super(EntityType.SHEEP, ((CraftWorld) owner.getWorld()).getHandle());

        pet = petEntity;

        goalSelector.removeAllGoals();
        targetSelector.removeAllGoals();

        goalSelector.addGoal(0, new LookAtPlayerGoal(this, ServerPlayer.class, 8.0F));
        goalSelector.addGoal(1, new PathFinderGoalPetsBasic(petEntity, this, speed, (float) ((EntitySettings) petEntity.getPetSettings()).getDistanceInBlock(), 2.3F));

        setTarget((LivingEntity) ((CraftEntity) owner).getHandle(), EntityTargetEvent.TargetReason.CUSTOM, false);
    }

    @Override
    public void travel(Vec3 vec3d) {
        if (isVehicle()) {
            LivingEntity entityliving = (LivingEntity) passengers.get(0);

            if (entityliving != null) {
                this.setYRot(entityliving.getYRot());
                this.yRotO = this.getYRot();
                this.setXRot(entityliving.getXRot() * 0.5F);
                this.setRot(this.getYRot(), this.getXRot());
                this.yBodyRot = this.getYRot();
                this.yHeadRot = this.yBodyRot;
                float sideMot = entityliving.xxa * ((float) pet.getPetSettings().getSpeed() / 3);
                float forMot = entityliving.zza * ((float) pet.getPetSettings().getSpeed() / 3);

                double y = 0;

                if (onGround) {
                    try {
                        Field jump = LivingEntity.class.getDeclaredField("bn");
                        jump.setAccessible(true);
                        if (jump.getBoolean(entityliving)) {
                            y = 0.5;
                            setDeltaMovement(vec3d.x, y, vec3d.z);
                        }
                    } catch (Exception ignored) {
                    }
                }

                maxUpStep = 1.0F;
                vec3d = new Vec3(sideMot, y, forMot);
            }
        }

        super.travel(vec3d);
    }

}
