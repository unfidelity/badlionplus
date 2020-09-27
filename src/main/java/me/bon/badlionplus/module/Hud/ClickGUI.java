package me.bon.badlionplus.module.Hud;

import me.bon.badlionplus.clickgui.ClickGui;
import me.bon.badlionplus.module.Category;
import me.bon.badlionplus.module.Module;

public class ClickGUI extends Module {
	public ClickGUI() {
		super("ClickGUI", Category.Hud);
	}

	
	@Override
    public void onEnable() {
        if (mc.player != null && mc.world != null) {
            mc.displayGuiScreen(new ClickGui());
            toggle();
        }
    }
	

	
}
