package me.bon.badlionplus.module.Combat;

import java.util.List;

import com.mojang.realmsclient.gui.ChatFormatting;

import me.bon.badlionplus.BadlionMod;
import me.bon.badlionplus.command.Command;
import me.bon.badlionplus.friends.Friends;
import me.bon.badlionplus.module.Category;
import me.bon.badlionplus.module.Module;
import me.bon.badlionplus.module.ModuleManager;
import me.bon.badlionplus.setting.Setting;
import me.bon.badlionplus.util.Messages;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;

public class PacketAutoCity extends Module {
	public PacketAutoCity() {
		super("PacketAutoCity", Category.Combat);
	}

	private boolean firstRun;
	private BlockPos mineTarget = null;
	private EntityPlayer closestTarget;
	
	Setting range;
	Setting announceUsage;
	
	@Override
	public void setup() {
		rSetting(range = new Setting("Range", this, 7, 0, 9, true, "range"));
		rSetting(announceUsage = new Setting("Announce Usage", this, true, "announceUsage"));
	}
	
	@Override
	public void onEnable() {
		if(mc.player == null) {
			this.toggle();
			return;
		}
		MinecraftForge.EVENT_BUS.register(this);
		firstRun = true;
	}
	
	@Override
	public void onDisable() {
		if(mc.player == null) {
			return;
		}
		MinecraftForge.EVENT_BUS.unregister(this);
		Messages.sendMessagePrefix(TextFormatting.BLUE + "[" + TextFormatting.GOLD + "AutoCity" + TextFormatting.BLUE + "]" + ChatFormatting.RED.toString() + " Disabled");
	}
	
	@Override
    public void onUpdate() {
		if(mc.player == null) {
			return;
		}
		findClosestTarget();

        if (closestTarget == null) {
            if (firstRun) {
                firstRun = false;
                if (announceUsage.getValBoolean()) {
                    Messages.sendMessagePrefix(TextFormatting.BLUE + "[" + TextFormatting.GOLD + "AutoCity" + TextFormatting.BLUE + "]" + ChatFormatting.WHITE.toString() + "Enabled" + ChatFormatting.RESET.toString() + ", no one to city!");
                }    
            }
            this.toggle();
            return;
        }
        
        if (firstRun && mineTarget != null) {
            firstRun = false;
            if (announceUsage.getValBoolean()) {
                Messages.sendMessagePrefix(TextFormatting.BLUE + "[" + TextFormatting.GOLD + "AutoCity" + TextFormatting.BLUE + "]" + TextFormatting.WHITE + " Attempting to mine: " + ChatFormatting.BLUE.toString() + closestTarget.getName());
            }
        }
        
        findCityBlock();
        if(mineTarget != null) {
        	int newSlot = -1;
            for (int i = 0; i < 9; i++) {
                ItemStack stack = mc.player.inventory.getStackInSlot(i);
                if (stack == ItemStack.EMPTY) {
                    continue;
                }
                if ((stack.getItem() instanceof ItemPickaxe)) {
                    newSlot = i;
                    break;
                }
            }
            if (newSlot != -1) {
                mc.player.inventory.currentItem = newSlot;
            }
        	boolean wasEnabled = BadlionMod.moduleManager.getModuleByName("PacketMine").isToggled();
        	if(!wasEnabled) {
        		BadlionMod.moduleManager.getModuleByName("PacketMine").toggle();
        	}
        	mc.player.swingArm(EnumHand.MAIN_HAND);
        	mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, mineTarget, EnumFacing.DOWN));
        	mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, mineTarget, EnumFacing.DOWN));
        	if(!wasEnabled) {
        		BadlionMod.moduleManager.getModuleByName("PacketMine").toggle();
        		//why would u ever have packet mine off its so good doe
        	}
        	this.toggle();
        } else {
        	Messages.sendMessagePrefix(TextFormatting.BLUE + "[" + TextFormatting.GOLD + "AutoCity" + TextFormatting.BLUE + "]" + " No city blocks to mine!");
        	this.toggle();
        }
        
	}
	
	public BlockPos findCityBlock() {
		Double dist = this.range.getValDouble();
		Vec3d vec = closestTarget.getPositionVector();
		if(mc.player.getPositionVector().distanceTo(vec) <= dist) {
			BlockPos targetX = new BlockPos(vec.add(1, 0, 0));
			BlockPos targetXMinus = new BlockPos(vec.add(-1, 0, 0));
			BlockPos targetZ = new BlockPos(vec.add(0, 0, 1));
			BlockPos targetZMinus = new BlockPos(vec.add(0, 0, -1));
			if(canBreak(targetX)) {
				mineTarget = targetX;
			}
			if(!canBreak(targetX) && canBreak(targetXMinus)) {
				mineTarget = targetXMinus;
			}
			if(!canBreak(targetX) && !canBreak(targetXMinus) && canBreak(targetZ)) {
				mineTarget = targetZ;
			}
			if(!canBreak(targetX) && !canBreak(targetXMinus) && !canBreak(targetZ) && canBreak(targetZMinus)) {
				mineTarget = targetZMinus;
			}
			if((!canBreak(targetX) && !canBreak(targetXMinus) && !canBreak(targetZ) && !canBreak(targetZMinus)) || mc.player.getPositionVector().distanceTo(vec) > dist) {
				mineTarget = null;
			} 
		}
		return mineTarget;
	}
	
	private boolean canBreak(BlockPos pos) {
		final IBlockState blockState = mc.world.getBlockState(pos);
		final Block block = blockState.getBlock();
		return block.getBlockHardness(blockState, mc.world, pos) != -1;
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
	
	public static boolean isLiving(Entity e) {
        return e instanceof EntityLivingBase;
    }
	
}
