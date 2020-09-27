package me.bon.badlionplus.module.Combat;

import me.bon.badlionplus.module.Category;
import me.bon.badlionplus.module.Module;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;

public class AutoTotem extends Module {
	public AutoTotem() {
		super("AutoTotem", Category.Combat);				
	}
	
	@Override
    public void onUpdate() {
		if(mc.player != null && mc.world != null && !(mc.currentScreen instanceof GuiContainer)) {
        if (mc.player.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND).getItem() == Items.TOTEM_OF_UNDYING) {
            return;
        }

        final int slot = this.getItemSlot();

        if (slot != -1) {
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 45, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, mc.player);
            mc.playerController.updateController();
        }
		}
    }
    private int getItemSlot() {
        for (int i = 0; i < 36; i++) {
            final Item item = mc.player.inventory.getStackInSlot(i).getItem();
            if (item == Items.TOTEM_OF_UNDYING) {
                if (i < 9) {
                    i += 36;
                }
                return i;
            }
        }
        return -1;
    }
}
