package me.bon.badlionplus.module.Render;

import me.bon.badlionplus.module.Category;
import me.bon.badlionplus.module.Module;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.Objects;

public class Fullbright extends Module {
	public Fullbright() {
		super("Fullbright", Category.Render);

		this.tickDelay = 1000;
	}


	@Override
	public void onUpdate() {
		if (mc.player == null) return;

		mc.player.addPotionEffect(new PotionEffect(Objects.requireNonNull(Potion.getPotionById(16))));
	}

	@Override
	public void onDisable() {
		mc.player.removeActivePotionEffect(Objects.requireNonNull(Potion.getPotionById(16)));
	}
}
