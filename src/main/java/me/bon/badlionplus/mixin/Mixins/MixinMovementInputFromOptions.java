package me.bon.badlionplus.mixin.Mixins;

import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import me.bon.badlionplus.BadlionMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;

@Mixin(value = MovementInputFromOptions.class, priority = 10000)
public abstract class MixinMovementInputFromOptions extends MovementInput {
    @Redirect(method = "updatePlayerMoveState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/settings/KeyBinding;isKeyDown()Z"))
    public boolean isKeyPressed(KeyBinding keyBinding) {
        if (BadlionMod.moduleManager.getModuleByName("GuiMove").isToggled()
                && Minecraft.getMinecraft().currentScreen != null
                && !(Minecraft.getMinecraft().currentScreen instanceof GuiChat)
                && Minecraft.getMinecraft().player != null) {
            return Keyboard.isKeyDown(keyBinding.getKeyCode());
        }
        return keyBinding.isKeyDown();
    }
}