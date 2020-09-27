package me.bon.badlionplus.clickgui.component;

import static me.bon.badlionplus.BadlionMod.moduleManager;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import me.bon.badlionplus.BadlionMod;
import me.bon.badlionplus.clickgui.ClickGui;
import me.bon.badlionplus.clickgui.component.components.Button;
import me.bon.badlionplus.module.Category;
import me.bon.badlionplus.module.Module;
import me.bon.badlionplus.util.ConfigManager;
import me.bon.badlionplus.util.Font.FontUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

public class Frame {

	public ArrayList<Component> components;
	public Category category;
	private boolean open;
	private int width;
	private int y;
	private int x;
	private int barHeight;
	private boolean isDragging;
	public int dragX;
	public int dragY;
	
	public Frame(Category cat) {
		this.components = new ArrayList<Component>();
		this.category = cat;
		this.width = 90;
		this.x = 8;
		this.y = 8;
		this.barHeight = 12;
		this.dragX = 0;
		this.open = true;
		this.isDragging = false;
		int tY = this.barHeight;
		
		
		for (Module mod : moduleManager.getModules()) {
			if (mod.getCategory().equals(category)){
				Button modButton = new Button(mod, this, tY);
				this.components.add(modButton);
				tY += 13;
			}
		}
	}
	
	public ArrayList<Component> getComponents() {
		return components;
	}
	
	public void setX(int newX) {
		this.x = newX;

	}
	
	public void setY(int newY) {
		this.y = newY;

	}
	
	public void setDrag(boolean drag) {
		this.isDragging = drag;
	}
	
	public boolean isOpen() {
		return open;
	}
	
	public void setOpen(boolean open) {
		this.open = open;
	}
	
	public void renderFrame(FontRenderer fontRenderer) {
		Gui.drawRect(this.x - 2, this.y - 2, this.x + this.width + 2, this.y + this.barHeight + 2, 0xFF424242);
		Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.barHeight, 0xFF272727);
		GL11.glPushMatrix();
		GL11.glScalef(1, 1, 1);
		BadlionMod.fontRenderer.drawStringWithShadow(this.category.name(), (float)(this.x + 11) + 13, (this.y + 1.2f), 0xFFFFFF);
		//BadlionMod.fontRenderer.drawCenteredString(this.category.name(), (this.x), (this.y), 0x2c76d1);
		GL11.glPopMatrix();
		if (this.open) {
			if (!this.components.isEmpty()) {
				//Gui.drawRect(this.x, this.y + this.barHeight, this.x + 1, this.y + this.barHeight + (12 * components.size()), new Color(0, 200, 20, 150).getRGB());
				//Gui.drawRect(this.x, this.y + this.barHeight + (12 * components.size()), this.x + this.width, this.y + this.barHeight + (12 * components.size()) + 1, new Color(0, 200, 20, 150).getRGB());
				//Gui.drawRect(this.x + this.width, this.y + this.barHeight, this.x + this.width - 1, this.y + this.barHeight + (12 * components.size()), new Color(0, 200, 20, 150).getRGB());
				for (Component component : components) {
					component.renderComponent();
				}
			}
		}
	}
	
	public void refresh() {
		int off = this.barHeight;
		for (Component comp : components) {
			comp.setOff(off);
			off += comp.getHeight();
		}
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void updatePosition(int mouseX, int mouseY) {
		if (this.isDragging) {
			this.setX(mouseX - dragX);
			this.setY(mouseY - dragY);
		}
	}
	
	public boolean isWithinHeader(int x, int y) {
		if (x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.barHeight) {
			return true;
		}
		return false;
	}
}