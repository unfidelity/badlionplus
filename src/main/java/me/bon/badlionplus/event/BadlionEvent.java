package me.bon.badlionplus.event;

import me.zero.alpine.type.Cancellable;
import net.minecraft.client.Minecraft;

public class BadlionEvent extends Cancellable {
	private Era era = Era.PRE;
    private final float partialTicks;

    public BadlionEvent() {
        partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();
    }

    public Era getEra() {
        return era;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public enum Era {
        PRE, PERI, POST
    }
}