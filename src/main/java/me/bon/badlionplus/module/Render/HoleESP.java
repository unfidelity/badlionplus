package me.bon.badlionplus.module.Render;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.lwjgl.opengl.GL11;

import me.bon.badlionplus.event.Events.RenderEvent;
import me.bon.badlionplus.module.Category;
import me.bon.badlionplus.module.Module;
import me.bon.badlionplus.setting.Setting;
import me.bon.badlionplus.util.BadlionTessellator;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class HoleESP extends Module {
	public HoleESP() {
		super("HoleESP (WIP)", Category.Render);
	}

	Setting rangeS;
    Setting r;
    Setting g;
    Setting b;
    Setting a;

    private final BlockPos[] surroundOffset = {
            new BlockPos(0, -1, 0), // down
            new BlockPos(0, 0, -1), // north
            new BlockPos(1, 0, 0), // east
            new BlockPos(0, 0, 1), // south
            new BlockPos(-1, 0, 0) // west
    };
    
    private ConcurrentHashMap<BlockPos, Boolean> safeHoles;
    
	@Override
	public void setup() {
		rSetting(rangeS = new Setting("Range", this, 7, 0, 9, true, "range"));
		rSetting(r = new Setting("Red", this, 255, 0, 255, true, "red"));
		rSetting(g = new Setting("Green", this, 255, 0, 255, true, "green"));
		rSetting(b = new Setting("Blue", this, 255, 0, 255, true, "blue"));
	}
	
	@Override
    public void onUpdate() {
		if(mc.player == null && mc.world == null) return;
		try {
        if (safeHoles == null) {
            safeHoles = new ConcurrentHashMap<>();
        } else {
            safeHoles.clear();
        }

        int range = (int) Math.ceil(rangeS.getValDouble());
        
        List<BlockPos> blockPosList = getSphere(getPlayerPos(), range, range, false, true, 0);

        for (BlockPos pos : blockPosList) {

            // block gotta be air
            if (!mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR)) {
                continue;
            }

            // block 1 above gotta be air
            if (!mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals(Blocks.AIR)) {
                continue;
            }

            // block 2 above gotta be air
            if (!mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals(Blocks.AIR)) {
                continue;
            }

            boolean isSafe = true;
            boolean isBedrock = true;

            for (BlockPos offset : surroundOffset) {
                Block block = mc.world.getBlockState(pos.add(offset)).getBlock();
                if (block != Blocks.BEDROCK) {
                    isBedrock = false;
                }
                if (block != Blocks.BEDROCK && block != Blocks.OBSIDIAN && block != Blocks.ENDER_CHEST && block != Blocks.ANVIL) {
                    isSafe = false;
                    break;
                }
            }

            if (isSafe) {
                safeHoles.put(pos, isBedrock);
            }
        }
		} catch(Exception bruh) {}
	}
	
	@Override
    public void render(final RenderEvent event) {
        if (mc.player == null || safeHoles == null) {
            return;
        }
        try {
        if (safeHoles.isEmpty()) {
            return;
        }

            BadlionTessellator.prepare("lines");

        safeHoles.forEach((blockPos, isBedrock) -> {
            BadlionTessellator.draw_cube_line(blockPos, (int)r.getValDouble(), (int)g.getValDouble(), (int)b.getValDouble(), (int)a.getValDouble(), "all");
        });
		} catch(Exception ignored) {
			
		}
    }
	
	public List<BlockPos> getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y) {
        List<BlockPos> circleblocks = new ArrayList<>();
        int cx = loc.getX();
        int cy = loc.getY();
        int cz = loc.getZ();
        for (int x = cx - (int) r; x <= cx + r; x++) {
            for (int z = cz - (int) r; z <= cz + r; z++) {
                for (int y = (sphere ? cy - (int) r : cy); y < (sphere ? cy + r : cy + h); y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < r * r && !(hollow && dist < (r - 1) * (r - 1))) {
                        BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                }
            }
        }
        return circleblocks;
    }

    public BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(Minecraft.getMinecraft().player.posX), Math.floor(Minecraft.getMinecraft().player.posY), Math.floor(Minecraft.getMinecraft().player.posZ));
    }
}
