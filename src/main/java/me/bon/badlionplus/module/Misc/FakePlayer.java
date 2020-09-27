package me.bon.badlionplus.module.Misc;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.apache.commons.io.IOUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;

import me.bon.badlionplus.module.Category;
import me.bon.badlionplus.module.Module;
import me.bon.badlionplus.util.Messages;
import net.minecraft.client.entity.EntityOtherPlayerMP;

public class FakePlayer extends Module {
	public FakePlayer() {
		super("FakePlayer", Category.Misc);
	}

	private EntityOtherPlayerMP _fakePlayer;

    @Override
    public void onEnable()
    {
        super.onEnable();
        _fakePlayer = null;

        if (mc.world == null)
        {
            this.toggle();
            return;
        }

        // If getting uuid from mojang doesn't work we use another uuid
        try
        {
            _fakePlayer = new EntityOtherPlayerMP(mc.world, new GameProfile(UUID.fromString(getUuid("jared2013")), "jared2013"));
        }
        catch (Exception e)
        {
            _fakePlayer = new EntityOtherPlayerMP(mc.world, new GameProfile(UUID.fromString("70ee432d-0a96-4137-a2c0-37cc9df67f03"), "jared2013"));
            Messages.sendMessagePrefix("Failed to load uuid, setting another one.");
        }
        Messages.sendMessagePrefix(String.format("%s has been spawned.", "jared2013"));

        mc.world.addEntityToWorld(_fakePlayer.getEntityId(), _fakePlayer);
        _fakePlayer.attemptTeleport(mc.player.posX, mc.player.posY, mc.player.posZ); // moves fake player to your current position
    }

    @Override
    public void onDisable()
    {
    	if(!(mc.world == null)) {
    		mc.world.removeEntity(_fakePlayer);
    	}
    }


    // Getting uuid from a name
    public static String getUuid(String name)
    {
        JsonParser parser = new JsonParser();
        String url = "https://api.mojang.com/users/profiles/minecraft/" + name;
        try
        {
            String UUIDJson = IOUtils.toString(new URL(url), StandardCharsets.UTF_8);
            if(UUIDJson.isEmpty()) return "invalid name";
            JsonObject UUIDObject = (JsonObject) parser.parse(UUIDJson);
            return reformatUuid(UUIDObject.get("id").toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "error";
    }

    // Reformating a short uuid type into a long uuid type
    private static String reformatUuid(String uuid)
    {
        String longUuid = "";

        longUuid += uuid.substring(1, 9) + "-";
        longUuid += uuid.substring(9, 13) + "-";
        longUuid += uuid.substring(13, 17) + "-";
        longUuid += uuid.substring(17, 21) + "-";
        longUuid += uuid.substring(21, 33);

        return longUuid;
    }
	
}
