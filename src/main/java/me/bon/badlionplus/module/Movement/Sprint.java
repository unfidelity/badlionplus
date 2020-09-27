package me.bon.badlionplus.module.Movement;

import me.bon.badlionplus.module.Category;
import me.bon.badlionplus.module.Module;
import me.bon.badlionplus.setting.Setting;

public class Sprint extends Module {
	public Sprint() {
		super("Sprint", Category.Movement);
	}

	@Override
	public void onUpdate() {
		try {
			if(mc.gameSettings.keyBindForward.isKeyDown() && !(mc.player.collidedHorizontally)) {
				if(!mc.player.isSprinting()) {
					mc.player.setSprinting(true);
				}
			}
		} catch (Exception e) {
			//do nothing
		}
	}
	
}
