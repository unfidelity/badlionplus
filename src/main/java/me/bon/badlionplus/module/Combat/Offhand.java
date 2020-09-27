package me.bon.badlionplus.module.Combat;

import me.bon.badlionplus.module.Category;
import me.bon.badlionplus.module.Module;
import me.bon.badlionplus.setting.Setting;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;

import java.util.ArrayList;

/**
 * @author Reap
 *
 * can't have bon taking credit lmfao
 */
public class Offhand extends Module {
    public Setting stopInGUI;
    public Setting swordGap;
    public Setting soft;

    public Setting minHealth;

    public Setting mode;

    public Offhand() {
        super("Offhand", Category.Combat);
    }

    @Override public void setup() {
        ArrayList<String> modes = new ArrayList<>();

        modes.add("Crystal");
        modes.add("Gapple");
        modes.add("Bed");
        modes.add("Totem");

        rSetting(stopInGUI = new Setting("Stop In Containers", this, false, "stopInGUI"));
        rSetting(swordGap = new Setting("Sword Gap", this, true, "swordGap"));
        rSetting(soft = new Setting("Soft", this, false, "soft"));
        rSetting(minHealth = new Setting("Min Health", this, 16.0, 0.0, 36.0, false, ""));
        rSetting(mode = new Setting("Offhand Mode", this, "Crystal", modes, "mode"));
    }

    @Override public void onUpdate() {
        if (mc.player == null || mc.world == null) return;
        if (stopInGUI.getValBoolean() && mc.currentScreen != null) return;
        if (soft.getValBoolean() && (mc.player.getHeldItemOffhand() != null || !(mc.player.getHeldItemOffhand().getItem() == Items.AIR))) return;

        int itemSlot = getItemSlot();
        if (itemSlot == -1) return;

        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, itemSlot, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 45, 0, ClickType.PICKUP, mc.player);
    }

    private int getItemSlot() {
        Item itemToSearch = Items.TOTEM_OF_UNDYING;

        if (!(mc.player.getHealth() + mc.player.getAbsorptionAmount() <= minHealth.getValDouble())) {
            if (swordGap.getValBoolean() && mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_SWORD) {
                itemToSearch = Items.GOLDEN_APPLE;
            } else {
                switch (mode.getValString()) {
                    case "Crystal":
                        itemToSearch = Items.END_CRYSTAL;
                        break;
                    case "Gapple":
                        itemToSearch = Items.GOLDEN_APPLE;
                        break;
                    case "Bed":
                        itemToSearch = Items.BED;
                        break;
                }
            }
        }

        for (int i = 9; i < 36; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == itemToSearch) {
                return i < 9 ? i + 36 : i; // not needed but used as a safety measure.
            }
        }

        return -1;
    }
}
