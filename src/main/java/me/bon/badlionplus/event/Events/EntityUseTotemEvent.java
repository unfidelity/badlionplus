package me.bon.badlionplus.event.Events;

import me.bon.badlionplus.event.BadlionEvent;
import net.minecraft.entity.Entity;

public class EntityUseTotemEvent extends BadlionEvent{
	private Entity entity;

    public EntityUseTotemEvent(Entity entity) {
        super();
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }
}
