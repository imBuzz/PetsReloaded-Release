package me.imbuzz.dev.petsreloaded.nms.v1_19_4_R0_1.goals;

import me.imbuzz.dev.petsreloaded.core.objects.pets.PetEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

public class PathFinderGoalPetsBasic extends Goal {

    private final PetEntity petEntity; //OUR PET
    private final Mob basicEntity; //OUR PET
    private final double movementSpeed;
    private final float minDistance, maxDistance;
    private LivingEntity petOwner; //OWNER
    private double xPoint, yPoint, zPoint;

    public PathFinderGoalPetsBasic(PetEntity petEntity, Mob basicEntity, double speed, float distance, float minDistance) {
        this.petEntity = petEntity;
        this.movementSpeed = speed;
        this.maxDistance = distance * distance;
        this.minDistance = minDistance * minDistance;
        this.basicEntity = basicEntity;
    }

    @Override
    public boolean canUse() {
        if (petEntity.isBlocked()) return false;

        petOwner = basicEntity.getTarget();
        if (petOwner == null) return false;

        double distance = petOwner.distanceToSqr(basicEntity);
        if (distance < minDistance) return false;

        if (distance > maxDistance && petOwner.isOnGround()) {
            basicEntity.setPos(petOwner.getX(), petOwner.getY(), petOwner.getZ());
            return false;
        }

        xPoint = petOwner.getX(); //X
        yPoint = petOwner.getY(); //Y
        zPoint = petOwner.getZ(); //Z

        return true;
    }

    @Override
    public void start() {
        basicEntity.getNavigation().moveTo(xPoint, yPoint, zPoint, movementSpeed);
    }

    @Override
    public boolean canContinueToUse() {
        return !basicEntity.getNavigation().isDone() && petOwner.distanceToSqr(basicEntity) >= minDistance && distanceFromNavigation() <= minDistance;
    }

    @Override
    public void stop() {
        petOwner = null;
        basicEntity.getNavigation().stop();
    }

    private double distanceFromNavigation() {
        if (petOwner == null) return 0;
        double x = petOwner.getX() - xPoint;
        double y = petOwner.getY() - yPoint;
        double z = petOwner.getZ() - zPoint;
        return x * x + y * y + z * z;
    }

}
