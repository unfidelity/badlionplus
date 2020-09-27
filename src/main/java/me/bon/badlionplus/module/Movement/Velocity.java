package me.bon.badlionplus.module.Movement;

import me.bon.badlionplus.BadlionMod;
import me.bon.badlionplus.event.Events.PacketEvent;
import me.bon.badlionplus.module.Category;
import me.bon.badlionplus.module.Module;
import me.bon.badlionplus.util.Messages;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;


public class Velocity extends Module {
	public Velocity() {
		super("Velocity", Category.Movement);
	}
	
	@Override
	public void onEnable() {
		BadlionMod.EVENT_BUS.subscribe(this);
	}
	
	@Override
	public void onDisable() {
		BadlionMod.EVENT_BUS.unsubscribe(this);
	}
	
	@EventHandler
    private Listener<PacketEvent.Receive> receiveListener = new Listener<>(event -> {
        if(event.getPacket() instanceof SPacketEntityVelocity){
            if(((SPacketEntityVelocity) event.getPacket()).getEntityID() == mc.player.getEntityId())
                event.cancel();
        }
        if(event.getPacket() instanceof SPacketExplosion)
            event.cancel();
    });
}
