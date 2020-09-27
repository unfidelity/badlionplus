package me.bon.badlionplus.mixin.Mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.bon.badlionplus.BadlionMod;
import me.bon.badlionplus.util.ColorUtils;
import me.bon.badlionplus.util.Font.FontUtils;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;

@Mixin(GuiMainMenu.class)
public class MixinGuiMainMenu extends GuiScreen {

	@Inject(method = "drawScreen", at = @At("TAIL"), cancellable = true)
	public void drawText(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
		BadlionMod.fontRenderer.drawStringWithShadow("Badlion+", 2, 2, ColorUtils.GenRainbow());
	}
	
}
