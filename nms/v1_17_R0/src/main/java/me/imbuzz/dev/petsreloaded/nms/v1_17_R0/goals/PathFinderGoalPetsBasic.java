package me.imbuzz.dev.petsreloaded.nms.v1_17_R0.goals;

import me.imbuzz.dev.petsreloaded.core.objects.pets.PetEntity;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;

public class PathFinderGoalPetsBasic extends PathfinderGoal {

    private final PetEntity petEntity; //OUR PET
    private final EntityInsentient basicEntity; //OUR PET
    private final double movementSpeed;
    private final float minDistance, maxDistance;
    private EntityLiving petOwner; //OWNER
    private double xPoint, yPoint, zPoint;

    public PathFinderGoalPetsBasic(PetEntity petEntity, EntityInsentient basicEntity, double speed, float distance, float minDistance) {
        this.petEntity = petEntity;
        this.movementSpeed = speed;
        this.maxDistance = distance * distance;
        this.minDistance = minDistance * minDistance;
        this.basicEntity = basicEntity;
    }

    @Override
    public boolean a() {
        if (petEntity.isBlocked()) return false;

        petOwner = basicEntity.getGoalTarget();
        if (petOwner == null) return false;
        double distance = petOwner.f(basicEntity);
        if (distance < minDistance) {
            return false;
        } else if (petOwner.f(basicEntity) > maxDistance && petOwner.getBukkitEntity().isOnGround()) {
            basicEntity.setPosition(petOwner.locX(), petOwner.locY(), petOwner.locZ());
            return false;
        } else {
            xPoint = petOwner.locX(); //X
            yPoint = petOwner.locY(); //Y
            zPoint = petOwner.locZ(); //Z
        }

        return true;
    }

    @Override
    public void c() {
        basicEntity.getNavigation().a(xPoint, yPoint, zPoint, movementSpeed);
    }

    @Override
    public boolean b() {
        return !basicEntity.getNavigation().m() && petOwner.f(basicEntity) >= minDistance && distanceFromNavigation() <= minDistance;
    }

    @Override
    public void d() {
        petOwner = null;
        basicEntity.getNavigation().o();
    }

    private double distanceFromNavigation() {
        if (petOwner == null) return 0;
        double x = petOwner.locX() - xPoint;
        double y = petOwner.locY() - yPoint;
        double z = petOwner.locZ() - zPoint;
        return x * x + y * y + z * z;
    }
}
