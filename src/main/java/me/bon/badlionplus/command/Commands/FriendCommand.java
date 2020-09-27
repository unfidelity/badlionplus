package me.bon.badlionplus.command.Commands;

import me.bon.badlionplus.BadlionMod;
import me.bon.badlionplus.command.Command;
import me.bon.badlionplus.friends.Friends;
import me.bon.badlionplus.util.Messages;
import net.minecraft.util.text.TextFormatting;

public class FriendCommand extends Command {
	public FriendCommand() {
		super("Friend", new String[] {"friend"});
	}

    @Override
    public void onCommand(String[] args) {
    	if(args.length > 2) {
    		Messages.sendMessagePrefix("Usage : " + BadlionMod.prefix + "friend [add/del] [playername]");
    	}
    	try {
        if(args[1].equalsIgnoreCase("add")){
            if(Friends.isFriend(args[2])) {
                Messages.sendMessagePrefix(TextFormatting.AQUA + args[2] + TextFormatting.WHITE + " is already a friend!");
                return;
            }
            if(!Friends.isFriend(args[2])){
                BadlionMod.getInstance().friends.addFriend(args[2]);
                Messages.sendMessagePrefix("Added " + TextFormatting.AQUA + args[2] + TextFormatting.WHITE + " to friends list");
                BadlionMod.configManager.saveFriends();
            }

        }
        if(args[1].equalsIgnoreCase("del") || args[1].equalsIgnoreCase("remove")){
            if(!Friends.isFriend(args[2])){
                Messages.sendMessagePrefix(TextFormatting.AQUA + args[2] + TextFormatting.WHITE + " is not a friend!");
                return;
            } 
            if(Friends.isFriend(args[2])){
                BadlionMod.getInstance().friends.delFriend(args[2]);
                Messages.sendMessagePrefix("Removed " + TextFormatting.AQUA + args[2] + TextFormatting.WHITE + " from friends list");
                BadlionMod.configManager.saveFriends();
            }

        }
      } catch(ArrayIndexOutOfBoundsException e) {
    	  Messages.sendMessagePrefix("Usage : " + BadlionMod.prefix + "friend [add/del] [playername]");
    	  //god i fucking LOVE try catch
      }
    }
}
