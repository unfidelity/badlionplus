package me.bon.badlionplus.clickgui.component.components.sub;

import java.awt.Color;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import me.bon.badlionplus.BadlionMod;
import me.bon.badlionplus.clickgui.component.Component;
import me.bon.badlionplus.clickgui.component.components.Button;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class Keybind extends Component {

	private boolean hovered;
	private boolean binding;
	private Button parent;
	private int offset;
	private int x;
	private int y;
	
	public Keybind(Button button, int offset) {
		this.parent = button;
		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
		this.offset = offset;
	}
	
	@Override
	public void setOff(int newOff) {
		offset = newOff;
	}
	
	@Override
	public void renderComponent() {
		Gui.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth() * 1), parent.parent.getY() + offset + 12, this.hovered ? 0xFF434343 : 0xFF313131);
		Gui.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + 2, parent.parent.getY() + offset + 12, new Color(65,118,189).getRGB());
		GL11.glPushMatrix();
		GL11.glScalef(0.5f,0.5f, 0.5f);
		BadlionMod.fontRenderer.drawStringWithShadow(binding ? "Press a key..." : ("Bind: " + Keyboard.getKeyName(this.parent.mod.getKey())), (parent.parent.getX() + 7) * 2, (parent.parent.getY() + offset + 2) * 2 + 5, -1);
		GL11.glPopMatrix();
	}
	
	@Override
	public void updateComponent(int mouseX, int mouseY) {
		this.hovered = isMouseOnButton(mouseX, mouseY);
		this.y = parent.parent.getY() + offset;
		this.x = parent.parent.getX();
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if (isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
			this.binding = !this.binding;
		}
	}
	
	@Override
	public void keyTyped(char typedChar, int key) {
		if (this.binding) {
			this.parent.mod.setKey(key);
			this.binding = false;
			BadlionMod.configManager.SaveAll();
		}
	}
	
	public boolean isMouseOnButton(int x, int y) {
		if (x > this.x && x < this.x + 88 && y > this.y && y < this.y + 12) {
			return true;
		}
		return false;
	}
}