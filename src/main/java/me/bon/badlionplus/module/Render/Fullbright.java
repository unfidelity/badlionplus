package me.bon.badlionplus.module.Render;

import me.bon.badlionplus.module.Category;
import me.bon.badlionplus.module.Module;

public class Fullbright extends Module {
	public Fullbright() {
		super("Fullbright", Category.Render);
	}
	
	
	@Override
	public void onEnable() {
		mc.gameSettings.gammaSetting = 100000000.0F;
	}
	
	@Override
	public void onDisable() {
		mc.gameSettings.gammaSetting = 1.0F;
	}
	
	
}
