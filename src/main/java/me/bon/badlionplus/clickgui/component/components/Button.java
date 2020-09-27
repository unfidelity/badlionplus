package me.bon.badlionplus.clickgui.component.components;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import me.bon.badlionplus.BadlionMod;
import me.bon.badlionplus.clickgui.ClickGui;
import me.bon.badlionplus.clickgui.component.Component;
import me.bon.badlionplus.clickgui.component.Frame;
import me.bon.badlionplus.clickgui.component.components.sub.Checkbox;
import me.bon.badlionplus.clickgui.component.components.sub.Keybind;
import me.bon.badlionplus.clickgui.component.components.sub.ModeButton;
import me.bon.badlionplus.clickgui.component.components.sub.Slider;
import me.bon.badlionplus.module.Hud.ClickGUI;
import static me.bon.badlionplus.BadlionMod.settingsManager;

import me.bon.badlionplus.module.Module;
import me.bon.badlionplus.setting.Setting;
import me.bon.badlionplus.util.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class Button extends Component {
	ClickGUI click = new ClickGUI();
	public Module mod;
	public Frame parent;
	public int offset;
	private boolean isHovered;
	private ArrayList<Component> subcomponents;
	public boolean open;
	
	
	
	public Button(Module mod, Frame parent, int offset) {
		this.mod = mod;
		this.parent = parent;
		this.offset = offset;
		this.subcomponents = new ArrayList<Component>();
		this.open = false;
		int opY = offset + 17;
		if (settingsManager.getSettingsByMod(mod) != null) {
			for (Setting s : settingsManager.getSettingsByMod(mod)){
				if (s.isCombo()){
					this.subcomponents.add(new ModeButton(s, this, mod, opY));
					opY += 12;
				}
				if (s.isSlider()){
					this.subcomponents.add(new Slider(s, this, opY));
					opY += 12;
				}
				if (s.isCheck()){
					this.subcomponents.add(new Checkbox(s, this, opY));
					opY += 12;
				}
			}
		}
		this.subcomponents.add(new Keybind(this, opY));
	}
	
	@Override
	public void setOff(int newOff) {
		offset = newOff;
		int opY = offset + 15;
		for(Component comp : this.subcomponents) {
			comp.setOff(opY);
			opY += 12;
		}
	}
	
	@Override
	public void renderComponent() {
		Gui.drawRect(0, 0, 4320, 2, ColorUtils.GenRainbow());
		Gui.drawRect(parent.getX() -2, this.parent.getY() + 2 + this.offset, parent.getX() + parent.getWidth() + 2, this.parent.getY() + 15 + this.offset + 2, 0xFF424242);
		Gui.drawRect(parent.getX(), this.parent.getY() + 3 + this.offset, parent.getX() + parent.getWidth(), this.parent.getY() + 15 + this.offset, this.isHovered ? (this.mod.isToggled() ? new Color(61,110,190).getRGB() : 0xFF353535) : (this.mod.isToggled() ? new Color(61,110,160).getRGB() : 0xFF313131));
		GL11.glPushMatrix();
		GL11.glScalef(0.9f,0.9f, 0.9f);
		BadlionMod.fontRenderer.drawStringWithShadow(this.mod.getName(), (parent.getX() + 2) * (1/.9f), (parent.getY() + offset + 2) * (1/.9f) + 4, this.mod.isToggled() ? 0xFFFFFFFF : -1);
		GL11.glPopMatrix();

		if (this.open) {
			if (!this.subcomponents.isEmpty()) {
				for (Component comp : this.subcomponents) {
					comp.renderComponent();
				}
				Gui.drawRect(parent.getX() +2, parent.getY() + this.offset + 12, parent.getX() + 3, parent.getY() + this.offset + ((this.subcomponents.size()+1) * 12), ClickGui.color);
			}
		}
	}
	
	@Override
	public int getHeight() {
		if (this.open) {
			return (12 * (this.subcomponents.size() + 1));
		}
		return 12;
	}
	
	@Override
	public void updateComponent(int mouseX, int mouseY) {
		this.isHovered = isMouseOnButton(mouseX, mouseY);
		if (!this.subcomponents.isEmpty()) {
			for (Component comp : this.subcomponents) {
				comp.updateComponent(mouseX, mouseY);
			}
		}
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if (isMouseOnButton(mouseX, mouseY) && button == 0) {
			this.mod.toggle();
		}
		if (isMouseOnButton(mouseX, mouseY) && button == 1) {
			this.open = !this.open;
			this.parent.refresh();
		}
		for (Component comp : this.subcomponents) {
			comp.mouseClicked(mouseX, mouseY, button);
		}
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		for (Component comp : this.subcomponents) {
			comp.mouseReleased(mouseX, mouseY, mouseButton);
		}
	}
	
	@Override
	public void keyTyped(char typedChar, int key) {
		for (Component comp : this.subcomponents) {
			comp.keyTyped(typedChar, key);
		}
	}
	
	public boolean isMouseOnButton(int x, int y) {
		if (x > parent.getX() && x < parent.getX() + parent.getWidth() && y > this.parent.getY() + 3 + this.offset && y < this.parent.getY() + 15 + this.offset) {
			return true;
		}
		return false;
	}
}