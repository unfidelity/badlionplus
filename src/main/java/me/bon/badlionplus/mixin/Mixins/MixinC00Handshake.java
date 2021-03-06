package me.bon.badlionplus.mixin.Mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.bon.badlionplus.BadlionMod;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.handshake.client.C00Handshake;

@Mixin(C00Handshake.class)
public class MixinC00Handshake {
	
	@Shadow int protocolVersion;
    @Shadow String ip;
    @Shadow int port;
    @Shadow EnumConnectionState requestedState;
    //server getting finnessed lmao
    @Inject(method = "writePacketData", at = @At(value = "HEAD"), cancellable = true)
    public void writePacketData(PacketBuffer buf, CallbackInfo info) {
        if (BadlionMod.moduleManager.getModuleByName("NoHandshake").isToggled()) {
            info.cancel();
            buf.writeVarInt(protocolVersion);
            buf.writeString(ip);
            buf.writeShort(port);
            buf.writeVarInt(requestedState.getId());
        }
    }
}
