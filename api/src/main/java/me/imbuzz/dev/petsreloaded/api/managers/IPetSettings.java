package me.imbuzz.dev.petsreloaded.api.managers;

public interface IPetSettings {

    /**
     * @return Get the current tag for that pet
     */
    String getTag();

    /**
     * @return the pet name
     */
    String getPetName();

    /**
     * @return the permission needed in order to spawn the pet
     */
    String getPermission();

    /**
     * @return the current pet speed
     */
    double getSpeed();

    boolean isMountable();

    /**
     * @return the model name
     */
    String getModelName();


}
