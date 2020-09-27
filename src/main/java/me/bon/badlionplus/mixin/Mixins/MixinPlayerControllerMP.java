package me.bon.badlionplus.mixin.Mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.bon.badlionplus.BadlionMod;
import me.bon.badlionplus.event.Events.EventPlayerClickBlock;
import me.bon.badlionplus.event.Events.EventPlayerDamageBlock;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

@Mixin(PlayerControllerMP.class)
public class MixinPlayerControllerMP {
	
	@Inject(method = "onPlayerDamageBlock", at = @At("HEAD"), cancellable = true)
    public void onPlayerDamageBlock(BlockPos posBlock, EnumFacing directionFacing, CallbackInfoReturnable<Boolean> p_Info)
    {
        EventPlayerDamageBlock l_Event = new EventPlayerDamageBlock(posBlock, directionFacing);
        BadlionMod.EVENT_BUS.post(l_Event);
        if (l_Event.isCancelled())
        {
            p_Info.setReturnValue(false);
            p_Info.cancel();
        }
    }
	
	@Inject(method = "clickBlock", at = @At("HEAD"), cancellable = true)
    public void clickBlock(BlockPos loc, EnumFacing face, CallbackInfoReturnable<Boolean> callback)
    {
        EventPlayerClickBlock l_Event = new EventPlayerClickBlock(loc, face);

        BadlionMod.EVENT_BUS.post(l_Event);
        if (l_Event.isCancelled())
        {
            callback.setReturnValue(false);
            callback.cancel();
        }
    }
	
}
