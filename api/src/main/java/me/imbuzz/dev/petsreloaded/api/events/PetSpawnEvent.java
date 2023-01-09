package me.imbuzz.dev.petsreloaded.api.events;

import lombok.Getter;
import lombok.Setter;
import me.imbuzz.dev.petsreloaded.api.objects.IPetEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class PetSpawnEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private final IPetEntity petInstance;
    @Setter
    private boolean cancelled;

    public PetSpawnEvent(IPetEntity petInstance) {
        this.petInstance = petInstance;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

}
