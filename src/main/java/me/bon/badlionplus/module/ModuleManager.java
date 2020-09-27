package me.bon.badlionplus.module;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import me.bon.badlionplus.event.Events.RenderEvent;
import me.bon.badlionplus.module.Combat.AutoCrystal;
import me.bon.badlionplus.module.Combat.AutoTotem;
import me.bon.badlionplus.module.Combat.AutoTrap;
import me.bon.badlionplus.module.Combat.AutoTrapBed;
import me.bon.badlionplus.module.Combat.BedAura;
import me.bon.badlionplus.module.Combat.BowSpam;
import me.bon.badlionplus.module.Combat.Criticals;
import me.bon.badlionplus.module.Combat.PacketAutoCity;
import me.bon.badlionplus.module.Combat.Surround;
import me.bon.badlionplus.module.Exploit.EChestBackPack;
import me.bon.badlionplus.module.Exploit.NoHandshake;
import me.bon.badlionplus.module.Hud.Arraylist;
import me.bon.badlionplus.module.Hud.ClickGUI;
import me.bon.badlionplus.module.Misc.ChatSuffix;
import me.bon.badlionplus.module.Misc.FakePlayer;
import me.bon.badlionplus.module.Misc.TotemPopAnnouncer;
import me.bon.badlionplus.module.Movement.GuiMove;
import me.bon.badlionplus.module.Movement.Sprint;
import me.bon.badlionplus.module.Movement.Strafe;
import me.bon.badlionplus.module.Movement.Velocity;
import me.bon.badlionplus.module.Player.NoFall;
import me.bon.badlionplus.module.Player.PacketMine;
import me.bon.badlionplus.module.Render.Fullbright;
import me.bon.badlionplus.module.Render.HoleESP;
import me.bon.badlionplus.util.BadlionTessellator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class ModuleManager {
    private static ArrayList<Module> modules = new ArrayList<Module>();

    public ModuleManager() {
    	//Combat
    	modules.add(new AutoCrystal());
    	modules.add(new AutoTotem());
    	modules.add(new AutoTrap());
    	modules.add(new AutoTrapBed());
    	modules.add(new BedAura());
    	modules.add(new BowSpam());
    	modules.add(new Criticals());
    	modules.add(new PacketAutoCity());
    	modules.add(new Surround());
    	//Exploit
    	modules.add(new EChestBackPack());
    	modules.add(new NoHandshake());
    	//Hud
    	modules.add(new Arraylist());
    	modules.add(new ClickGUI());
    	//Misc
    	modules.add(new ChatSuffix());
    	modules.add(new FakePlayer());
    	modules.add(new TotemPopAnnouncer());
    	//Movement
    	modules.add(new GuiMove());
    	modules.add(new Sprint());
    	modules.add(new Strafe());
    	//Player
    	modules.add(new NoFall());
    	modules.add(new PacketMine());
    	modules.add(new Velocity());
    	//Render
    	modules.add(new Fullbright());
    	modules.add(new HoleESP());
    }

    public ArrayList<Module> getModules() {
        return modules;
    }

    public Module getModuleByName(String name) {
        return modules.stream().filter(module -> module.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
    
    public static Vec3d getInterpolatedAmount(Entity entity, double x, double y, double z) {
        return new Vec3d(
                (entity.posX - entity.lastTickPosX) * x,
                (entity.posY - entity.lastTickPosY) * y,
                (entity.posZ - entity.lastTickPosZ) * z
        );
    }
    
    public static Vec3d getInterpolatedAmount(Entity entity, double ticks) {
        return getInterpolatedAmount(entity, ticks, ticks, ticks);
    }
    
    public static Vec3d getInterpolatedPos(Entity entity, float ticks) {
        return new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).add(getInterpolatedAmount(entity, ticks));
    }
    
    public static void onWorldRender(RenderWorldLastEvent event) {
        Minecraft.getMinecraft().profiler.startSection("badlionplus");

        Minecraft.getMinecraft().profiler.startSection("setup");
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.disableDepth();

        GlStateManager.glLineWidth(1f);
        Vec3d renderPos = getInterpolatedPos(Minecraft.getMinecraft().player, event.getPartialTicks());

        RenderEvent e = new RenderEvent(BadlionTessellator.INSTANCE, renderPos);
        e.resetTranslation();
        Minecraft.getMinecraft().profiler.endSection();

        modules.stream().filter(module -> module.isToggled()).forEach(module -> {
            Minecraft.getMinecraft().profiler.startSection(module.getName());
            module.render(e);
            Minecraft.getMinecraft().profiler.endSection();
        });

        Minecraft.getMinecraft().profiler.startSection("release");
        GlStateManager.glLineWidth(1f);

        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.enableCull();
//        GlStateManager.popMatrix();
        BadlionTessellator.release_gl();
        Minecraft.getMinecraft().profiler.endSection();

        Minecraft.getMinecraft().profiler.endSection();
    }
    
}