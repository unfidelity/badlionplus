package me.bon.badlionplus.module.Movement;

import org.lwjgl.input.Keyboard;

import me.bon.badlionplus.module.Category;
import me.bon.badlionplus.module.Module;
import net.minecraft.client.gui.GuiChat;

public class GuiMove extends Module {
	public GuiMove() {
		super("GuiMove", Category.Movement);
	}

	@Override
	public void onUpdate() {
		if(mc.player != null && mc.world != null && mc.currentScreen != null && !(mc.currentScreen instanceof GuiChat)) {
			if(Keyboard.isKeyDown(200)) {
				mc.player.rotationPitch -= 5;
			}
			if(Keyboard.isKeyDown(208)) {
				mc.player.rotationPitch += 5;
			}
			
			if(Keyboard.isKeyDown(205)) {
				mc.player.prevRotationYaw += 5;
			}
			
			if(Keyboard.isKeyDown(203)) {
				mc.player.prevRotationYaw -=5;
			}
			
			if(mc.player.rotationPitch > 90)
				mc.player.rotationPitch = 90;
			if(mc.player.prevRotationPitch < -90)
				mc.player.rotationPitch = -90;
		}
	}
	
}
