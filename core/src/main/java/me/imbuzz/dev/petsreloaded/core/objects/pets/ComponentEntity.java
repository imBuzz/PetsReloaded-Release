package me.imbuzz.dev.petsreloaded.core.objects.pets;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

@Getter
public class ComponentEntity {

    private final Entity entity;
    private final PetEntity pet;
    @Setter
    private double distanceY = 0, distanceX = 0, distanceZ = 0;


    public ComponentEntity(Entity entity, PetEntity pet) {
        this.entity = entity;
        this.pet = pet;
    }

    public void move() {
        Location location = pet.getHeadEntity().getLocation().clone();
        Vector direction = getDirection(location);

        Location newLocation = location.add(direction.multiply(distanceX));
        float zvp = (float) (newLocation.getZ() + distanceZ * Math.sin(Math.toRadians(newLocation.getYaw())));
        float xvp = (float) (newLocation.getX() + distanceZ * Math.cos(Math.toRadians(newLocation.getYaw())));

        Location loc = new Location(pet.getHeadEntity().getWorld(), xvp, pet.getHeadEntity().getLocation().getY() + distanceY, zvp, newLocation.getYaw(), 0);

        entity.teleport(loc);
    }

    public void die() {
        entity.remove();
    }

    private Vector getDirection(Location location) {
        Vector vector = new Vector();
        vector.setX(-Math.sin(Math.toRadians(location.getYaw())));
        vector.setZ(Math.cos(Math.toRadians(location.getYaw())));
        return vector;
    }

}
