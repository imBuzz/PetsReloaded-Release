package me.imbuzz.dev.petsreloaded.api.events;

import lombok.Getter;
import lombok.Setter;
import me.imbuzz.dev.petsreloaded.api.objects.IPetEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class PetChangeSitEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private final IPetEntity petInstance;
    private final PositionType oldPosition;
    @Setter
    private boolean cancelled;
    @Setter
    private PositionType newPosition;

    public PetChangeSitEvent(IPetEntity petInstance, PositionType oldPosition, PositionType newPosition) {
        this.petInstance = petInstance;
        this.oldPosition = oldPosition;
        this.newPosition = newPosition;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public enum PositionType {
        BLOCKED,
        UNBLOCKED;

        public PositionType next() {
            int index = ordinal() + 1 >= PositionType.values().length ? 0 : ordinal() + 1;
            return PositionType.values()[index];
        }


    }

}
