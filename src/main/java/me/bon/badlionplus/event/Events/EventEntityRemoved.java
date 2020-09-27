package me.bon.badlionplus.event.Events;

import me.bon.badlionplus.event.BadlionEvent;
import net.minecraft.entity.Entity;

public class EventEntityRemoved extends BadlionEvent {
	private final Entity entity;

    public EventEntityRemoved(Entity entity) {
        this.entity = entity;
    }

    public Entity get_entity() {
        return this.entity;
    }
}
