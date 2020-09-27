package me.bon.badlionplus.command;

import java.util.HashSet;

import me.bon.badlionplus.BadlionMod;
import me.bon.badlionplus.command.Commands.Bind;
import me.bon.badlionplus.command.Commands.FriendCommand;
import me.bon.badlionplus.command.Commands.Help;
import me.bon.badlionplus.command.Commands.Toggle;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CommandManager {
    public static HashSet<Command> commands = new HashSet<>();

    public static void init() {
        commands.clear();
        //commands.add(new Class());
        commands.add(new Toggle());
        commands.add(new Bind());
        commands.add(new Help());
        commands.add(new FriendCommand());
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void chatEvent(ClientChatEvent event) {
        String[] args = event.getMessage().split(" ");
        if (event.getMessage().startsWith(BadlionMod.prefix)) {
            event.setCanceled(true);
            for (Command c: commands){
                if (args[0].equalsIgnoreCase(BadlionMod.prefix + c.getCommand())){
                    c.onCommand(args);
                }
            }
        }
    }
}