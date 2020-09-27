package me.bon.badlionplus.command.Commands;

import org.lwjgl.input.Keyboard;

import com.mojang.realmsclient.gui.ChatFormatting;

import me.bon.badlionplus.BadlionMod;
import me.bon.badlionplus.command.Command;
import me.bon.badlionplus.module.Module;
import me.bon.badlionplus.util.Messages;

public class Bind extends Command {
    public Bind() {
        super("bind", new String[]{"b", "bind"});
    }
    @Override
    public void onCommand(String[] args) {
        if (args.length > 2) {
            try {
                for (Module m: BadlionMod.moduleManager.getModules()) {
                    if (m.getName().equalsIgnoreCase(args[1])) {
                        try {
                            m.setKey(Keyboard.getKeyIndex(args[2].toUpperCase()));
                            Messages.sendMessagePrefix(ChatFormatting.WHITE + m.getName() + ChatFormatting.WHITE + " is now binded to " + ChatFormatting.GREEN + args[2].toUpperCase());
                        } catch (Exception e) {
                            Messages.sendMessagePrefix(ChatFormatting.RED + m.getName() + ChatFormatting.WHITE + " Caught some exception e");
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}