package me.bon.badlionplus.setting;

import java.awt.Color;
import java.util.ArrayList;

import me.bon.badlionplus.module.Module;

public class Setting {

    private String displayName;
    private String id;
    private Module parent;
    private String mode;

    private String sval;
    private ArrayList<String> options;

    private boolean bval;

    private double dval;
    private double min;
    private double max;
    private boolean onlyint = false;

    private Color color;

    private String customVal;

    public Setting(String displayName, Module parent, String sval, ArrayList<String> options, String id) {
        this.displayName = displayName;
        this.parent = parent;
        this.sval = sval;
        this.options = options;
        this.mode = "Combo";
        this.id = id;
    }

    public Setting(String displayName, Module parent, boolean bval, String id) {
        this.displayName = displayName;
        this.parent = parent;
        this.bval = bval;
        this.mode = "Check";
        this.id = id;
    }

    public Setting(String displayName, Module parent, double dval, double min, double max, boolean onlyint, String id) {
        this.displayName = displayName;
        this.parent = parent;
        this.dval = dval;
        this.min = min;
        this.max = max;
        this.onlyint = onlyint;
        this.mode = "Slider";
        this.id = id;
    }

    public Setting(String displayName, Module parent, Color color, String id) {
        this.displayName = displayName;
        this.parent = parent;
        this.color = color;
        this.mode = "ColorPicker";
        this.id = id;
    }

    public Setting(String displayName, Module parent, String customVal, String id) {
        this.displayName = displayName;
        this.parent = parent;
        this.customVal = customVal;
        this.mode = "CustomString";
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getId() {
        return id;
    }

    public Module getParentMod() {
        return parent;
    }

    public String getValString() {
        return this.sval;
    }

    public void setValString(String in) {
        this.sval = in;
    }

    public ArrayList<String> getOptions() {
        return this.options;
    }

    public boolean getValBoolean() {
        return this.bval;
    }

    public void setValBoolean(boolean in) {
        this.bval = in;
    }

    public double getValDouble(){
        if (this.onlyint) {
            this.dval = (int)dval;
        }
        return this.dval;
    }

    public int getValInt() {
        return (int)getValDouble();
    }

    public void setValDouble(double in) {
        this.dval = in;
    }

    public double getMin() {
        return this.min;
    }

    public double getMax() {
        return this.max;
    }

    public boolean isCombo() {
        return this.mode.equalsIgnoreCase("Combo");
    }

    public boolean isCheck() {
        return this.mode.equalsIgnoreCase("Check");
    }

    public boolean isSlider() {
        return this.mode.equalsIgnoreCase("Slider");
    }

    public boolean isColorPicker() {
        return mode.equalsIgnoreCase("ColorPicker");
    }

    public boolean isCustomString() {
        return mode.equalsIgnoreCase("CustomString");
    }

    public boolean onlyInt() {
        return this.onlyint;
    }

    public Color getValColor() {
        return color;
    }

    public void setValColor(Color newColor) {
        color = newColor;
    }

    public int getColorRed() {
        return color.getRed();
    }
    public int getColorGreen() {
        return color.getGreen();
    }
    public int getColorBlue() {
        return color.getBlue();
    }

    public int getColorRgb() {
        return color.getRGB();
    }

    public String getCustomVal() {
        return customVal;
    }

    public void setCustomVal(String newString) {
        customVal = newString;
    }
}