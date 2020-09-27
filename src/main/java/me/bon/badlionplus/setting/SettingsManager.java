package me.bon.badlionplus.setting;

import java.util.ArrayList;

import me.bon.badlionplus.module.Module;

public class SettingsManager {

    private ArrayList<Setting> settings;

    public SettingsManager() {
        this.settings = new ArrayList<>();
    }

    public void rSetting(Setting in) {
        this.settings.add(in);
    }

    public ArrayList<Setting> getSettings() {
        return this.settings;
    }

    public ArrayList<Setting> getSettingsByMod(Module mod) {
        ArrayList<Setting> out = new ArrayList<>();
        for (Setting s : getSettings()) {
            if (s.getParentMod().equals(mod)) {
                out.add(s);
            }
        }
        return out;
    }

    public Setting getSettingByDisplayName(String name) {
        for (Setting set : getSettings()) {
            if (set.getDisplayName().equalsIgnoreCase(name)) {
                return set;
            }
        }
        System.err.println("[Badlion+] Setting not found Error: '" + name +"'!");
        return null;
    }

    public Setting getSettingByID(String id) {
        for (Setting s : getSettings()) {
            if (s.getId().equalsIgnoreCase(id)) {
                return s;
            }
        }
        System.err.println("[Badlion+] Setting not found Error: '" + id +"'!");
        return null;
    }
}