package me.bon.badlionplus;

import java.awt.Font;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import me.bon.badlionplus.command.CommandManager;
import me.bon.badlionplus.friends.Friends;
import me.bon.badlionplus.module.Module;
import me.bon.badlionplus.module.ModuleManager;
import me.bon.badlionplus.module.Combat.Criticals;
import me.bon.badlionplus.setting.SettingsManager;
import me.bon.badlionplus.util.ConfigManager;
import me.bon.badlionplus.util.Font.CFontRenderer;
import me.zero.alpine.EventBus;
import me.zero.alpine.EventManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

//most of the credits to this client can be found in mcmod.info
//thanks to everyone i copped code from

//might do a more original rewrite if i can be asked

@Mod(modid = BadlionMod.MODID, name = BadlionMod.NAME, version = BadlionMod.VERSION)
public class BadlionMod
{
    public static final String MODID = "badlionplus";
    public static final String NAME = "Badlion+";
    public static final String VERSION = "1.4.4";
    
    public static CFontRenderer fontRenderer;
    
    public static SettingsManager settingsManager;
    public static ModuleManager moduleManager;
    public static ConfigManager configManager;
    public Friends friends;
    public static final EventBus EVENT_BUS = new EventManager();
    
    public static String prefix = ",";
    
    @Mod.Instance
    private static BadlionMod INSTANCE;
    
    public BadlionMod() {
    	INSTANCE = this;
    }
    
    public static BadlionMod getInstance() {
    	return INSTANCE;
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	Display.setTitle(NAME + " v" + VERSION );
    	friends = new Friends();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
    	settingsManager = new SettingsManager();
    	moduleManager = new ModuleManager();
    	configManager = new ConfigManager();
    	fontRenderer = new CFontRenderer(new Font("Comfortaa", Font.PLAIN, 20), true, false);
    	CommandManager.init();
    	MinecraftForge.EVENT_BUS.register(new CommandManager());
    	MinecraftForge.EVENT_BUS.register(new Criticals());
    	MinecraftForge.EVENT_BUS.register(this);
    }
    
    @SubscribeEvent
    public void onKeyPress(InputEvent.KeyInputEvent event) {
        for (Module m: moduleManager.getModules()) {
            if (Keyboard.isKeyDown(m.getKey())) {
                m.toggle();
            }
        }
    }

    
}
