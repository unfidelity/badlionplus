package me.bon.badlionplus.mixin.Mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.bon.badlionplus.BadlionMod;
import me.bon.badlionplus.event.Events.EventEntityRemoved;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

@Mixin(value = World.class)
public class MixinWorld {
	@Inject(method = "onEntityRemoved", at = @At("HEAD"), cancellable = true)
    public void onEntityRemoved(Entity event_packet, CallbackInfo p_Info)
    {
        EventEntityRemoved l_Event = new EventEntityRemoved(event_packet);

        BadlionMod.EVENT_BUS.post(l_Event);

    }
}
