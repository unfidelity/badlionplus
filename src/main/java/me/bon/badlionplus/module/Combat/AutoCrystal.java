package me.bon.badlionplus.module.Combat;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import me.bon.badlionplus.BadlionMod;
import me.bon.badlionplus.event.Events.EventEntityRemoved;
import me.bon.badlionplus.event.Events.PacketEvent;
import me.bon.badlionplus.event.Events.RenderEvent;
import me.bon.badlionplus.friends.Friends;
import me.bon.badlionplus.module.Category;
import me.bon.badlionplus.module.Module;
import me.bon.badlionplus.setting.Setting;
import me.bon.badlionplus.util.BadlionPair;
import me.bon.badlionplus.util.BadlionTessellator;
import me.bon.badlionplus.util.BlockUtils;
import me.bon.badlionplus.util.ColorUtils;
import me.bon.badlionplus.util.CrystalUtil;
import me.bon.badlionplus.util.EntityUtil;
import me.bon.badlionplus.util.Messages;
import me.bon.badlionplus.util.Timer;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;

public class AutoCrystal extends Module {
	public AutoCrystal() {
		super("AutoCrystal", Category.Combat);
	}
	
	Setting placeCA;
	Setting breakCA;
	Setting breakAttempts;
	Setting antiWeakness;
	Setting breakRange;
	Setting placeRange;
	Setting wallRange;
	Setting breakDelay;
	Setting minDamagePlace;
	Setting minDamageBreak;
	Setting selfDamage;
	Setting antiSui;
	Setting autoSwitch;
	Setting antiStuck;
	Setting newServ;
	Setting faceplace;
	Setting faceplaceHealth;
	Setting faceplaceCheck;
	Setting render;
	Setting r;
	Setting g;
	Setting b;
	
	@Override
	public void setup() {
		rSetting(placeCA = new Setting("Place", this, true, "place"));
		rSetting(breakCA = new Setting("Break", this, true, "break"));
		rSetting(autoSwitch = new Setting("Auto-Switch", this, true, "autoswitch"));
		rSetting(antiWeakness = new Setting("Anti-Weakness", this, false, "antiweakness"));
		rSetting(antiSui = new Setting("Anti-Suicide", this, true, "antisui"));
		rSetting(antiStuck = new Setting("Anti-Stuck", this, true, "antistuck"));
		rSetting(newServ = new Setting("1.13+ Mode", this, false, "newServ"));
		rSetting(faceplace = new Setting("FacePlace Mode", this, false, "faceplace"));
		rSetting(faceplaceCheck = new Setting("Disable FP w/ Sword", this, true, "faceplacewsword"));
		rSetting(render = new Setting("Render (BROKEN!!!)", this, false, "brokenrender"));
		
		rSetting(placeRange = new Setting("Place Range", this, 8, 1, 9, true, "placerange"));
		rSetting(breakRange = new Setting("Break Range", this, 8, 1, 9, true, "breakrange"));
		rSetting(wallRange = new Setting("Wall Range", this, 6, 1, 6, true, "wallrange"));
		rSetting(minDamagePlace = new Setting("Min Damage Place", this, 8, 0, 20, true, "dmgplace"));
		rSetting(minDamageBreak = new Setting("Min Damage Break", this, 6, 0, 20, true, "dmgbreak"));
		rSetting(selfDamage = new Setting("Max Self Damage", this, 6, 0, 20, true, "dmgself"));
		rSetting(breakAttempts = new Setting("Break Attempts", this, 2, 1, 6, true, "breakattempts"));
		rSetting(faceplaceHealth = new Setting("Faceplace Min Health", this, 8, 0, 36, true, "faceplaceHealth"));
		rSetting(r = new Setting("Red", this, 255, 0, 255, true, "r"));
		rSetting(g = new Setting("Green", this, 255, 0, 255, true, "g"));
		rSetting(b = new Setting("Blue", this, 255, 0, 255, true, "b"));
		
	}
	
	private final ConcurrentHashMap<EntityEnderCrystal, Integer> attackedCrystals = new ConcurrentHashMap<>();
	
	private EntityPlayer autoEzTarget = null;


    private BlockPos renderBlockInit;

    private double renderDamageValue;

    private float yaw;
    private float pitch;

    private boolean alreadyAttacking = false;
    private boolean placeTimeoutFlag = false;
    private boolean isRotating;
    private boolean didAnything;
    private boolean outline;
    private boolean solid;

    private int chainStep = 0;
    private int currentChainIndex = 0;
    private int placeTimeout;
    private int breakTimeout;
    private int breakDelayCounter;
    private int placeDelayCounter;
    
    private final Timer timer = new Timer();
    private final Timer removeVisualTimer = new Timer();
    
    @Override
    public void onEnable() {
    	MinecraftForge.EVENT_BUS.register(this);
    	BadlionMod.EVENT_BUS.subscribe(this);
    	placeTimeout = 10;
        breakTimeout = 10;
        placeTimeoutFlag = false;
        isRotating = false;
        autoEzTarget = null;
        chainStep = 0;
        currentChainIndex = 0;
        timer.reset();
        removeVisualTimer.reset();
    }
    
    @Override
    public void onDisable() {
    	MinecraftForge.EVENT_BUS.unregister(this);
    	BadlionMod.EVENT_BUS.unsubscribe(this);
        renderBlockInit = null;
        autoEzTarget = null;
    }
    
    @Override
    public void onUpdate() {
    	do_ca();
    }
    
    @EventHandler
    private Listener<EventEntityRemoved> on_entity_removed = new Listener<>(event -> {
        if (event.get_entity() instanceof EntityEnderCrystal) {
            attackedCrystals.remove(event.get_entity());
        }
    });
    
    @EventHandler
    private final Listener<PacketEvent.Receive> receive_listener = new Listener<>(event -> {
        if (event.getPacket() instanceof SPacketSoundEffect) {
            final SPacketSoundEffect packet = (SPacketSoundEffect) event.getPacket();

            if (packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                for (Entity e : mc.world.loadedEntityList) {
                    if (e instanceof EntityEnderCrystal) {
                        if (e.getDistance(packet.getX(), packet.getY(), packet.getZ()) <= 6.0f) {
                            e.setDead();
                        }
                    }
                }
            }
        }

    });
    
    public void do_ca() {
        didAnything = false;

        if (mc.player == null || mc.world == null) return;

        if (removeVisualTimer.passed(1000)) {
            removeVisualTimer.reset();
            attackedCrystals.clear();
        }

        if (checkPause()) {
            return;
        }

        if (placeCA.getValBoolean() && placeDelayCounter > placeTimeout) {
            place_crystal();
        }

        if (breakCA.getValBoolean() && breakDelayCounter > breakTimeout) {
            break_crystal();
        }

        if (!didAnything) {

            autoEzTarget = null;
            isRotating = false;
        }


        if (timer.passed(1000)) {
            timer.reset();
            chainStep = 0;
        }

        breakDelayCounter++;
        placeDelayCounter++;
    }
    
    public EntityEnderCrystal get_best_crystal() {

        double best_damage = 0;

        double minimum_damage;
        double maximum_damage_self = this.selfDamage.getValDouble();

        double best_distance = 0;

        EntityEnderCrystal best_crystal = null;
        try {
        for (Entity c : mc.world.loadedEntityList) {

            if (!(c instanceof EntityEnderCrystal)) continue;

            EntityEnderCrystal crystal = (EntityEnderCrystal) c;
            if (mc.player.getDistance(crystal) > (!mc.player.canEntityBeSeen(crystal) ? this.wallRange.getValInt() : this.breakRange.getValInt())) {
                continue;
            }

            if (crystal.isDead) continue;

            if (attackedCrystals.containsKey(crystal) && attackedCrystals.get(crystal) > 5 && antiStuck.getValBoolean()) continue;

            for (Entity player : mc.world.playerEntities) {

                if (player == mc.player || !(player instanceof EntityPlayer)) continue;

                if (Friends.isFriend(player.getName())) continue;

                if (player.getDistance(mc.player) >= 11) continue;

                final EntityPlayer target = (EntityPlayer) player;

                if (target.isDead || target.getHealth() <= 0) continue;

                boolean no_place = faceplaceCheck.getValBoolean() && mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_SWORD;
                if ((target.getHealth() < faceplaceHealth.getValInt() && faceplace.getValBoolean() && !no_place) || !no_place) {
                    minimum_damage = 2;
                } else {
                    minimum_damage = this.minDamageBreak.getValInt();
                }

                final double target_damage = CrystalUtil.calculateDamage(crystal, target);

                if (target_damage < minimum_damage) continue;

                final double self_damage = CrystalUtil.calculateDamage(crystal, mc.player);

                if (self_damage > maximum_damage_self || (antiSui.getValBoolean() && (mc.player.getHealth() + mc.player.getAbsorptionAmount()) - self_damage <= 0.5)) continue;

                if (target_damage > best_damage) {
                    autoEzTarget = target;
                    best_damage = target_damage;
                    best_crystal = crystal;
                }

            }

            if (mc.player.getDistanceSq(crystal) > best_distance) {
                best_distance = mc.player.getDistanceSq(crystal);
                best_crystal = crystal;
            }

        }
        }catch(ConcurrentModificationException cme) {
        }
        return best_crystal;

    }
    
    
    public BlockPos get_best_block() {

        if (get_best_crystal() != null) {
            placeTimeoutFlag = true;
            return null;
        }

        if (placeTimeoutFlag) {
            placeTimeoutFlag = false;
            return null;
        }

        List<BadlionPair<Double, BlockPos>> damage_blocks = new ArrayList<>();
        double best_damage = 0;
        double minimum_damage;
        double maximum_damage_self = this.selfDamage.getValDouble();

        BlockPos best_block = null;

        List<BlockPos> blocks = CrystalUtil.possiblePlacePositions((float) placeRange.getValInt(), newServ.getValBoolean(), true);

        for (Entity player : mc.world.playerEntities) {

            if (Friends.isFriend(player.getName())) continue;

            for (BlockPos block : blocks) {

                if (player == mc.player || !(player instanceof EntityPlayer)) continue;

                if (player.getDistance(mc.player) >= 11) continue;

                if (!BlockUtils.rayTracePlaceCheck(block, false)) {
                    continue;
                }

                if (!BlockUtils.canSeeBlock(block) && mc.player.getDistance(block.getX(), block.getY(), block.getZ()) > wallRange.getValInt()) {
                    continue;
                }

                final EntityPlayer target = (EntityPlayer) player;

                if (target.isDead || target.getHealth() <= 0) continue;

                boolean no_place = faceplace.getValBoolean() && mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_SWORD;
                if ((target.getHealth() < faceplaceHealth.getValInt() && faceplace.getValBoolean() && !no_place) || !no_place) {
                    minimum_damage = 2;
                } else {
                    minimum_damage = this.minDamagePlace.getValInt();
                }

                final double target_damage = CrystalUtil.calculateDamage((double) block.getX() + 0.5, (double) block.getY() + 1, (double) block.getZ() + 0.5, target);

                if (target_damage < minimum_damage) continue;

                final double self_damage = CrystalUtil.calculateDamage((double) block.getX() + 0.5, (double) block.getY() + 1, (double) block.getZ() + 0.5, mc.player);

                if (self_damage > maximum_damage_self || (antiSui.getValBoolean() && (mc.player.getHealth() + mc.player.getAbsorptionAmount()) - self_damage <= 0.5)) continue;

                /** if (attempt_chain.get_value(true) && chain_step > 0) {
                    damage_blocks.add(new BadlionPair<>(best_damage, best_block));
                    autoez_target = target;
                } else**/ if (target_damage > best_damage) {
                    best_damage = target_damage;
                    best_block = block;
                    autoEzTarget = target;
                }

            }

        }

        blocks.clear();

        if (chainStep == 1) {
            currentChainIndex = 3;
        } else if (chainStep > 1) {
            currentChainIndex--;
        }

        renderDamageValue = best_damage;
        renderBlockInit = best_block;

        damage_blocks = sortBestBlocks(damage_blocks);

        //if (!attempt_chain.get_value(true)) {
            return best_block;
//        } else {
//            if (damage_blocks.size() == 0) {
//                return null;
//            }
//            if (damage_blocks.size() < current_chain_index) {
//                return damage_blocks.get(0).getValue();
//            }
//            return damage_blocks.get(current_chain_index).getValue();
//        }

    }
    
    public List<BadlionPair<Double, BlockPos>> sortBestBlocks(List<BadlionPair<Double, BlockPos>> list) {
        List<BadlionPair<Double, BlockPos>> new_list = new ArrayList<>();
        double damage_cap = 1000;
        for (int i = 0; i < list.size(); i++) {
            double biggest_dam = 0;
            BadlionPair<Double, BlockPos> best_pair = null;
            for (BadlionPair<Double, BlockPos> pair : list) {
                if (pair.getKey() > biggest_dam && pair.getKey() < damage_cap) {
                    best_pair = pair;
                }
            }
            if (best_pair == null) continue;
            damage_cap = best_pair.getKey();
            new_list.add(best_pair);
        }
        return new_list;
    }
    
    public void place_crystal() {

        BlockPos target_block = get_best_block();

        if (target_block == null) {
            return;
        }

        placeDelayCounter = 0;

        alreadyAttacking = false;

        boolean offhand_check = false;
        if (mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
            if (mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL && autoSwitch.getValBoolean()) {
                if (find_crystals_hotbar() == -1) return;
                mc.player.inventory.currentItem = find_crystals_hotbar();
                return;
            }
        } else {
            offhand_check = true;
        }


        chainStep++;
        didAnything = true;
        timer.reset();
        BlockUtils.placeCrystalOnBlock(target_block, offhand_check ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);

    }
    
    private int find_crystals_hotbar() {
        for (int i = 0; i < 9; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == Items.END_CRYSTAL) {
                return i;
            }
        }
        return -1;
    }

    public void break_crystal() {

        EntityEnderCrystal crystal = get_best_crystal();
        if (crystal == null) {
            return;
        }

        if (antiWeakness.getValBoolean() && mc.player.isPotionActive(MobEffects.WEAKNESS)) {

            boolean should_weakness = true;

            if (mc.player.isPotionActive(MobEffects.STRENGTH)) {

                if (Objects.requireNonNull(mc.player.getActivePotionEffect(MobEffects.STRENGTH)).getAmplifier() == 2) {
                    should_weakness = false;
                }

            }

            if (should_weakness) {

                if (!alreadyAttacking) {
                    alreadyAttacking = true;
                }

                int new_slot = -1;

                for (int i = 0; i < 9; i++) {

                    ItemStack stack = mc.player.inventory.getStackInSlot(i);

                    if (stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemTool) {
                        new_slot = i;
                        mc.playerController.updateController();
                        break;
                    }

                }

                if (new_slot != -1) {
                    mc.player.inventory.currentItem = new_slot;
                }

            }

        }

        didAnything = true;

        for (int i = 0; i < breakAttempts.getValInt(); i++) {
            EntityUtil.attackEntity(crystal, false);
        }
        addAttackedCrystal(crystal);

        breakDelayCounter = 0;

    }

    private void addAttackedCrystal(EntityEnderCrystal crystal) {

        if (attackedCrystals.containsKey(crystal)) {
            int value = attackedCrystals.get(crystal);
            attackedCrystals.put(crystal, value + 1);
        } else {
            attackedCrystals.put(crystal, 1);
        }


    }
    
    public boolean checkPause() {

        if (find_crystals_hotbar() == -1 && mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
            return true;
        }

        if (mc.gameSettings.keyBindAttack.isKeyDown() && mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe) {
        	renderBlockInit = null;
            return true;
        }

        return false;
    }
    
    @Override
    public void render(RenderEvent event) {
	if(render.getValBoolean()) {
        if (renderBlockInit == null) return;

            outline = false;
            solid = true;

        render_block(renderBlockInit);
	}
    }
    
    public void render_block(BlockPos pos) {
        BlockPos render_block = (pos);

        float h = 1.0F;

        if (solid) {
            BadlionTessellator.prepare("quads");
            BadlionTessellator.draw_cube(BadlionTessellator.get_buffer_build(),
                    render_block.getX(), render_block.getY(), render_block.getZ(),
                    1, h, 1,
                    r.getValInt(), g.getValInt(), b.getValInt(), 255,
                    "all"
            );
            BadlionTessellator.release();
        }

        if (outline) {
            BadlionTessellator.prepare("lines");
            BadlionTessellator.draw_cube_line(BadlionTessellator.get_buffer_build(),
                    render_block.getX(), render_block.getY(), render_block.getZ(),
                    1, h, 1,
                    r.getValInt(), g.getValInt(), b.getValInt(), 255,
                    "all"
            );
            BadlionTessellator.release();
        }
    }
    

    
}
