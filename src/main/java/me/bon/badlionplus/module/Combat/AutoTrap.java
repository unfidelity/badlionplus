package me.bon.badlionplus.module.Combat;

import java.util.List;

import me.bon.badlionplus.friends.Friends;
import me.bon.badlionplus.module.Category;
import me.bon.badlionplus.module.Module;
import me.bon.badlionplus.setting.Setting;
import me.bon.badlionplus.util.Messages;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;

public class AutoTrap extends Module {
	public AutoTrap() {
		super("AutoTrap", Category.Combat);
	}
	//this module is basically the same as surround but for other people
	//ez paste
	private int lastSlot;
	private int Obby;
	private int tickDelay;
	private boolean firstRun;
	private String lastTickTargetName;
	private EntityPlayer closestTarget;

	Setting range;
	Setting timeOutTicks;
	Setting announceUsage;
	
	@Override
	public void setup() {
		rSetting(range = new Setting("Range", this, 5, 3, 8, true, "range"));
		rSetting(timeOutTicks = new Setting("Timeout Ticks", this, 15, 5, 20, true, "timeOutTicks"));
		rSetting(announceUsage = new Setting("Announce Usage", this, true, "AnnounceUsage"));
	}
	
	@Override
	public void onEnable() {
		if(mc.player == null) {
			return;
		}
		MinecraftForge.EVENT_BUS.register(this);
		
		lastSlot = mc.player.inventory.currentItem;
		
		firstRun = true;
		
		tickDelay = 0;

	}
	
	@Override
	public void onDisable() {
		if(mc.player == null) {
			return;
		}
		if(lastSlot != -1) {
		mc.player.inventory.currentItem = lastSlot;
		}
		
		tickDelay = 0;
		
		if(announceUsage.getValBoolean()) {
			Messages.sendMessagePrefix(TextFormatting.BLUE + "[" + TextFormatting.GOLD + "AutoTrap" + TextFormatting.BLUE + "]" + TextFormatting.RED + " disabled!");
		}
		
	}
	
	@Override
	public void onUpdate() {
		if(mc.player == null) {
			return;
		}
		
		try {
			findClosestTarget();
			} catch(NullPointerException npe) {}
		
		if(closestTarget == null) {
			if(firstRun) {
				firstRun = false;
				if(announceUsage.getValBoolean()) {
					Messages.sendMessagePrefix(TextFormatting.BLUE + "[" + TextFormatting.GOLD + "AutoTrap" + TextFormatting.BLUE + "]" + TextFormatting.WHITE + " enabled, " + TextFormatting.WHITE + "waiting for target.");
				}
			}
		}
		
		if (firstRun && closestTarget != null) {
            firstRun = false;
            lastTickTargetName = closestTarget.getName();
            if (announceUsage.getValBoolean()) {
                Messages.sendMessagePrefix(TextFormatting.BLUE + "[" + TextFormatting.GOLD + "AutoTrap" + TextFormatting.BLUE + "]" + TextFormatting.WHITE + " enabled" + TextFormatting.WHITE + ", target: " + lastTickTargetName);
            }
        } 
		
		if(closestTarget != null && lastTickTargetName != null) {
			if (!lastTickTargetName.equals(closestTarget.getName())) {
				lastTickTargetName = closestTarget.getName();
				if (announceUsage.getValBoolean()) {
					Messages.sendMessagePrefix(TextFormatting.BLUE + "[" + TextFormatting.GOLD + "AutoTrap" + TextFormatting.BLUE + "]" + " New target: " + lastTickTargetName);
				}
			}
		}
		if(closestTarget != null && mc.player.getPositionVector().distanceTo(closestTarget.getPositionVector()) <= range.getValInt()) {
		Obby = findObby();
		tickDelay++;
			//maybe make this some kind of arraylist instead of doing each position one at a time
			BlockPos targetBX = new BlockPos(closestTarget.getPositionVector().add(1, 0, 0));
			BlockPos targetBMinusX = new BlockPos(closestTarget.getPositionVector().add(-1, 0, 0));
			BlockPos targetBZ = new BlockPos(closestTarget.getPositionVector().add(0, 0, 1));
			BlockPos targetBMinusZ = new BlockPos(closestTarget.getPositionVector().add(0, 0, -1));
			BlockPos targetTX = new BlockPos(closestTarget.getPositionVector().add(1, 1, 0));
			BlockPos targetTMinusX = new BlockPos(closestTarget.getPositionVector().add(-1, 1, 0));
			BlockPos targetTZ = new BlockPos(closestTarget.getPositionVector().add(0, 1, 1));
			BlockPos targetTMinusZ = new BlockPos(closestTarget.getPositionVector().add(0, 1, -1));
			BlockPos targetTT = new BlockPos(closestTarget.getPositionVector().add(0, 3, 0));
		if(Obby > 1) {
				mc.player.inventory.currentItem = Obby;
				if(tickDelay <= timeOutTicks.getValInt() * 3) {
					if(canPlace(targetBX)) {
						placeBlock(targetBX, EnumFacing.DOWN);
					}
					if(canPlace(targetBMinusX)) {
						placeBlock(targetBMinusX, EnumFacing.DOWN);
					}
					if(canPlace(targetBZ)) {
						placeBlock(targetBZ, EnumFacing.DOWN);
					}
					if(canPlace(targetBMinusZ)) {
						placeBlock(targetBMinusZ, EnumFacing.DOWN);
					}
					if(canPlace(targetTX)) {
						placeBlock(targetTX, EnumFacing.DOWN);
					}
					if(canPlace(targetTMinusX)) {
						placeBlock(targetTMinusX, EnumFacing.DOWN);
					}
					if(canPlace(targetTZ)) {
						placeBlock(targetTZ, EnumFacing.DOWN);
					}	
					if(canPlace(targetTMinusZ)) {
						placeBlock(targetTMinusZ, EnumFacing.DOWN);
					}
					if(canPlace(targetTT)) {
						placeBlock(targetTT, EnumFacing.DOWN);
					}
				}
			
				if(tickDelay > timeOutTicks.getValInt() * 3) {
					this.toggle();
				}
		}
		}
		
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

	private void findClosestTarget() {
        List<EntityPlayer> playerList = mc.world.playerEntities;
        closestTarget = null;
        for (EntityPlayer target : playerList) {

            if (target == mc.player) {
                continue;
            }
            if (Friends.isFriend(target.getName())) {
                continue;
            }
            if (!isLiving(target)) {
                continue;
            }
            if ((target).getHealth() <= 0) {
                continue;
            }
            if (closestTarget == null) {
                closestTarget = target;
                continue;
            }
            if (mc.player.getDistance(target) < mc.player.getDistance(closestTarget)) {
                closestTarget = target;
            }
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
	
	public static boolean isLiving(Entity e) {
        return e instanceof EntityLivingBase;
    }
	
}
