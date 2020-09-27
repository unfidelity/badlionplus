package me.bon.badlionplus.module.Misc;

import me.bon.badlionplus.BadlionMod;
import me.bon.badlionplus.module.Category;
import me.bon.badlionplus.module.Module;

import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatSuffix extends Module {
    public ChatSuffix() {
        super("ChatSuffix", Category.Misc);
    }

    @Override
    public void onEnable() {
    	MinecraftForge.EVENT_BUS.register(this);
    }
    
    @Override
    public void onDisable() {
    	MinecraftForge.EVENT_BUS.unregister(this);
    }
    
    @SubscribeEvent
    public void onChat(ClientChatEvent event) {
        String suffix = "  \uff5c \u0299\u1d00\u1d05\u029f\u026a\u1d0f\u0274+";
        if (event.getMessage().startsWith("/")) return;
        if(event.getMessage().startsWith("!")) return;
        if (event.getMessage().startsWith(BadlionMod.prefix)) return;
        if (event.getMessage().contains(suffix)) return;
        event.setMessage(event.getMessage() + suffix);
    }
}
