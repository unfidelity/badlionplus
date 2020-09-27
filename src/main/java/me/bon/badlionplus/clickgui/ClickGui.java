package me.bon.badlionplus.clickgui;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import me.bon.badlionplus.BadlionMod;
import me.bon.badlionplus.clickgui.component.Component;
import me.bon.badlionplus.clickgui.component.Frame;
import me.bon.badlionplus.module.Category;
import me.bon.badlionplus.util.Messages;
import net.minecraft.client.gui.GuiScreen;
import scala.collection.generic.BitOperations.Int;

//Credit to HeroCode/Osiris/Obamahack
//Guis are hard asf this helped me a lot

public class ClickGui extends GuiScreen {

	public static ArrayList<Frame> frames;
	public static int color = 0x2e3033;
	private static int newX;
	private static int newY;
	private boolean firstopen = false;
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		for (Frame frame : frames) {
			frame.renderFrame(this.fontRenderer);
			frame.updatePosition(mouseX, mouseY);
			for (Component comp : frame.getComponents()) {
				comp.updateComponent(mouseX, mouseY);
			}
		}
		

	}
	
	
	public ClickGui() {
		this.frames = new ArrayList<Frame>();
		int frameX = 5;
		for (Category category : Category.values()) {
			Frame frame = new Frame(category);
			frame.setX(frameX);
			frames.add(frame);
			frameX += frame.getWidth() + 10;
		}
		
		
	}
	@Override
	public void initGui() {
		try {
            File file = new File(BadlionMod.configManager.BadlionPlus.getAbsolutePath(), "FramePositions.txt");
            FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = br.readLine()) != null) {
                String curLine = line.trim();
                String name = curLine.split(":")[0];
                String xory = curLine.split(":")[1];
                String numba = curLine.split(":")[2];
                for (Frame frame:ClickGui.frames) {
                	if(xory.contains("x") && frame.category.ordinal() == 0 && name.contains("Combat")) {
                		frame.setX(Integer.parseInt(numba));
                	}
                	if(xory.contains("y") && frame.category.ordinal() == 0 && name.contains("Combat")) {
                		frame.setY(Integer.parseInt(numba));
                	}
                	if(xory.contains("x") && frame.category.ordinal() == 1 && name.contains("Exploit")) {
                		frame.setX(Integer.parseInt(numba));
                	}
                	if(xory.contains("y") && frame.category.ordinal() == 1 && name.contains("Exploit")) {
                		frame.setY(Integer.parseInt(numba));
                	}
                	if(xory.contains("x") && frame.category.ordinal() == 2 && name.contains("Hud")) {
                		frame.setX(Integer.parseInt(numba));
                	}
                	if(xory.contains("y") && frame.category.ordinal() == 2 && name.contains("Hud")) {
                		frame.setY(Integer.parseInt(numba));
                	}
                	if(xory.contains("x") && frame.category.ordinal() == 3 && name.contains("Misc")) {
                		frame.setX(Integer.parseInt(numba));
                	}
                	if(xory.contains("y") && frame.category.ordinal() == 3 && name.contains("Misc")) {
                		frame.setY(Integer.parseInt(numba));
                	}
                	if(xory.contains("x") && frame.category.ordinal() == 4 && name.contains("Movement")) {
                		frame.setX(Integer.parseInt(numba));
                	}
                	if(xory.contains("y") && frame.category.ordinal() == 4 && name.contains("Movement")) {
                		frame.setY(Integer.parseInt(numba));
                	}
                	if(xory.contains("x") && frame.category.ordinal() == 5 && name.contains("Player")) {
                		frame.setX(Integer.parseInt(numba));
                	}
                	if(xory.contains("y") && frame.category.ordinal() == 5 && name.contains("Player")) {
                		frame.setY(Integer.parseInt(numba));
                	}
                	if(xory.contains("x") && frame.category.ordinal() == 6 && name.contains("Render")) {
                		frame.setX(Integer.parseInt(numba));
                	}
                	if(xory.contains("y") && frame.category.ordinal() == 6 && name.contains("Render")) {
                		frame.setY(Integer.parseInt(numba));
                	}
                	
                }
                }
            br.close();
        } catch (Exception var11) {
        	var11.printStackTrace();
        }
	}

	
	@Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
		for (Frame frame : frames) {
			if (frame.isWithinHeader(mouseX, mouseY) && mouseButton == 0) {
				frame.setDrag(true);
				frame.dragX = mouseX - frame.getX();
				frame.dragY = mouseY - frame.getY();
			}
			if (frame.isWithinHeader(mouseX, mouseY) && mouseButton == 1) {
				frame.setOpen(!frame.isOpen());
			}
			if (frame.isOpen()) {
				if (!frame.getComponents().isEmpty()) {
					for (Component component : frame.getComponents()) {
						component.mouseClicked(mouseX, mouseY, mouseButton);
					}
				}
			}
		}
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) {
		for (Frame frame : frames) {
			if (frame.isOpen() && keyCode != 1) {
				if (!frame.getComponents().isEmpty()) {
					for (Component component : frame.getComponents()) {
						component.keyTyped(typedChar, keyCode);
					}
				}
			}
		}
		if (keyCode == 1) {
            this.mc.displayGuiScreen(null);
        }
	}

	
	@Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
		for (Frame frame : frames) {
			frame.setDrag(false);
		}
		for (Frame frame : frames) {
			if (frame.isOpen()) {
				if (!frame.getComponents().isEmpty()) {
					for (Component component : frame.getComponents()) {
						component.mouseReleased(mouseX, mouseY, state);
					}
				}
			}
		}
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}
