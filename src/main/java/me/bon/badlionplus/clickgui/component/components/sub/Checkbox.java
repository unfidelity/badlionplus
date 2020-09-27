package me.bon.badlionplus.clickgui.component.components.sub;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import me.bon.badlionplus.BadlionMod;
import me.bon.badlionplus.clickgui.component.Component;
import me.bon.badlionplus.clickgui.component.components.Button;
import me.bon.badlionplus.setting.Setting;
import me.bon.badlionplus.util.Messages;
import net.minecraft.client.gui.Gui;

public class Checkbox extends Component {

	private boolean hovered;
	private Setting setting;
	private Button parent;
	private int offset;
	private int x;
	private int y;
	
	public Checkbox(Setting setting, Button button, int offset) {
		this.setting = setting;
		this.parent = button;
		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
		this.offset = offset;
	}

	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		BadlionMod.configManager.SaveAll();
		if (isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
			
			this.setting.setValBoolean(!this.setting.getValBoolean());
					
		}
	}
	
	
	@Override
	public void renderComponent() {
		Gui.drawRect(parent.parent.getX() + 2, parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth() * 1), parent.parent.getY() + offset + 12, this.hovered ? 0xFF434343 : 0xFF313131);
		Gui.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + 2, parent.parent.getY() + offset + 12, new Color(65,118,189).getRGB());
		GL11.glPushMatrix();
		GL11.glScalef(0.5f,0.5f, 0.5f);
		BadlionMod.fontRenderer.drawStringWithShadow(this.setting.getDisplayName(), (parent.parent.getX() + 10 + 4) * 2 + 5, (parent.parent.getY() + offset + 2) * 2 + 4, -1);
		GL11.glPopMatrix();
		Gui.drawRect(parent.parent.getX() + 3 + 4, parent.parent.getY() + offset + 3, parent.parent.getX() + 9 + 4, parent.parent.getY() + offset + 9, 0xFF999999);
		if(this.setting.getValBoolean()) {
			Gui.drawRect(parent.parent.getX() + 4 + 4, parent.parent.getY() + offset + 4, parent.parent.getX() + 8 + 4, parent.parent.getY() + offset + 8, new Color(65,118,189).getRGB());
		}
	}
	
	@Override
	public void setOff(int newOff) {
		offset = newOff;
	}
	
	@Override
	public void updateComponent(int mouseX, int mouseY) {
		this.hovered = isMouseOnButton(mouseX, mouseY);
		this.y = parent.parent.getY() + offset;
		this.x = parent.parent.getX();
	}
	

	
	public boolean isMouseOnButton(int x, int y) {

		if (x > this.x && x < this.x + 88 && y > this.y && y < this.y + 12) {
			return true;
		}
		return false;
	}
}

