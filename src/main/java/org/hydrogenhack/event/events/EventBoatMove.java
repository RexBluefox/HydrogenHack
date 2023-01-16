package org.hydrogenhack.event.events;

import net.minecraft.entity.vehicle.BoatEntity;
import org.hydrogenhack.event.Event;

public class EventBoatMove extends Event {
    private static final EventBoatMove INSTANCE = new EventBoatMove();

    public BoatEntity boat;

    public static EventBoatMove get(BoatEntity entity) {
        INSTANCE.boat = entity;
        return INSTANCE;
    }
}
