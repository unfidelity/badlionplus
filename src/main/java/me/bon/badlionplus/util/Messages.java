package me.bon.badlionplus.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class Messages {
    public static String prefix = TextFormatting.BLUE + "[" + TextFormatting.GOLD + "Badlion+"  + TextFormatting.BLUE + "]" + TextFormatting.WHITE;
    private static final EntityPlayerSP player = Minecraft.getMinecraft().player;

    public static void sendRawMessage(String message) {
        player.sendMessage(new TextComponentString(message));
    }
    public static void sendMessagePrefix(String message) {
        sendRawMessage(prefix + " " + message);
    }
}