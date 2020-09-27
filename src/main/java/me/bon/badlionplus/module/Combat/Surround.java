package me.bon.badlionplus.module.Combat;

import me.bon.badlionplus.module.Category;
import me.bon.badlionplus.module.Module;
import me.bon.badlionplus.setting.Setting;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockObsidian;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;

public class Surround extends Module {
	public Surround() {
		super("Surround", Category.Combat);
	}
	
	Setting timeOutTicks;
	Setting selfAnvil;
	
	private int playerHotbarSlot = -1;
    private int anvilsPlaced;
    private int tickDelay;
    private int obby;
    private BlockPos anvilTarget;
    private BlockPos playerPos;
	
	@Override
	public void setup() {
		rSetting(timeOutTicks = new Setting("Timeout Ticks", this, 15, 5, 20, true, "timeOutTicks"));
		rSetting(selfAnvil = new Setting("Self Anvil", this, false, "SelfAnvil"));
	}

	
	@Override
	public void onEnable() {
		anvilsPlaced = 0;
		playerHotbarSlot = mc.player.inventory.currentItem;
		playerPos = new BlockPos(mc.player.getPositionVector());
		MinecraftForge.EVENT_BUS.register(this);
		int anvil = this.findAnvil();
		anvilTarget = new BlockPos(mc.player.getPositionVector().add(0, 3, 0));
		//so many checks bruh
		if(anvil > 0 && anvilsPlaced == 0 && selfAnvil.getValBoolean() && canPlace(playerPos) && canPlace(anvilTarget.add(0, -1, 0))) {
			mc.player.inventory.currentItem = anvil;
			placeBlock(anvilTarget, EnumFacing.DOWN);
			anvilsPlaced = 1;
			mc.player.inventory.currentItem = playerHotbarSlot;
		}
	}
	
	
	@Override
	public void onDisable() {
		if (mc.player == null) {
            return;
        }
		
		mc.player.inventory.currentItem = playerHotbarSlot;
		
		tickDelay = 0;


	}
	
	@Override
	public void onUpdate() {

		obby = findObby();
		
		tickDelay++;
		
		BlockPos surroundX = new BlockPos(mc.player.getPositionVector().add(1, 0, 0));
		BlockPos surroundXMinus = new BlockPos(mc.player.getPositionVector().add(-1, 0, 0));
		BlockPos surroundZ = new BlockPos(mc.player.getPositionVector().add(0, 0, 1));
		BlockPos surroundZMinus = new BlockPos(mc.player.getPositionVector().add(0, 0, -1));
		BlockPos bottom = new BlockPos(mc.player.getPositionVector().add(0, 0, 0));
		
		if(obby > 0) {
		mc.player.inventory.currentItem = findObby();
		} else {
			this.toggle();
		}
		
		if(tickDelay + 10 < timeOutTicks.getValInt() + 10) {
			if(canPlace(surroundX)) {
				placeBlock(surroundX, EnumFacing.DOWN);
			}
			
			if(canPlace(surroundXMinus)) {
				placeBlock(surroundXMinus, EnumFacing.DOWN);
			}
			
			if(canPlace(surroundZ)) {
				placeBlock(surroundZ, EnumFacing.DOWN);
			}
			
			if(canPlace(surroundZMinus)) {
				placeBlock(surroundZMinus, EnumFacing.DOWN);
			}
			if(canPlace(bottom)) {
				placeBlock(bottom, EnumFacing.DOWN);
			}
		}
		
		if(mc.player.ticksExisted % timeOutTicks.getValInt() == 0) {
			this.toggle();
		}
		
	}
	
	private int findObby() {
        int slot = -1;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack == ItemStack.EMPTY || !(stack.getItem() instanceof ItemBlock)) {
                continue;
            }
            Block block = ((ItemBlock) stack.getItem()).getBlock();
            if (block instanceof BlockObsidian) {
                slot = i;
                break;
            }
        }
        return slot;
    }
	
	private int findAnvil() {
        int slot = -1;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack == ItemStack.EMPTY || !(stack.getItem() instanceof ItemBlock)) {
                continue;
            }
            Block block = ((ItemBlock) stack.getItem()).getBlock();
            if (block instanceof BlockAnvil) {
                slot = i;
                break;
            }
        }
        return slot;
    }
	
	private boolean canPlace(BlockPos pos) {
        Block block = mc.world.getBlockState(pos).getBlock();
        if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid)) {
            return false;
        } else {
        	return true;
        }
	}
	
	private void placeBlock(BlockPos pos, EnumFacing side) {
        BlockPos neighbour = pos.offset(side);
        EnumFacing opposite = side.getOpposite();
        Vec3d hitVec = new Vec3d(neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        mc.playerController.processRightClickBlock(mc.player, mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
        mc.player.swingArm(EnumHand.MAIN_HAND);

    }


	
}
