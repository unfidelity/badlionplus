
package me.bon.badlionplus.util.Font;

import me.bon.badlionplus.BadlionMod;
import net.minecraft.client.Minecraft;

public class FontUtils {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public static float drawStringWithShadow(boolean customFont, String text, int x, int y, int color){
        if(customFont) return BadlionMod.fontRenderer.drawStringWithShadow(text, x, y, color);
        else return mc.fontRenderer.drawStringWithShadow(text, x, y, color);
    }
    
    public static float drawString(boolean customFont, String text, int x, int y, int color, boolean shadow) {
    	if(customFont) return BadlionMod.fontRenderer.drawString(text, x, y, color, shadow);
    	else return mc.fontRenderer.drawString(text, x, y, color, false);
    }
    
    public static float drawFloatedStringWithShadow(boolean customFont, String text, int x, float y, int color){
        if(customFont) return BadlionMod.fontRenderer.drawStringWithShadow(text, x, y, color);
        else return mc.fontRenderer.drawStringWithShadow(text, x, y, color);
    }
    
    public static float drawFloatedString(boolean customFont, String text, int x, float y, int color, boolean shadow){
        if(customFont) return BadlionMod.fontRenderer.drawString(text, x, y, color, shadow);
        else return mc.fontRenderer.drawString(text, x, y, color, shadow);
    }
    
    public static int getStringWidth(boolean customFont, String str){
        if(customFont) return BadlionMod.fontRenderer.getStringWidth(str);
        else return mc.fontRenderer.getStringWidth(str);
    }

    public static int getFontHeight(boolean customFont){
        if(customFont) return BadlionMod.fontRenderer.getHeight();
        else return mc.fontRenderer.FONT_HEIGHT;
    }
}