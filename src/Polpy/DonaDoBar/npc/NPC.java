package Polpy.DonaDoBar.npc;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityHeadRotation;
import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;

public class NPC {
	private static List<EntityPlayer> NPC = new ArrayList<EntityPlayer>();
	
	public static void createNPC(Player player, String skin) {
		MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
		WorldServer world = ((CraftWorld) Bukkit.getWorld(player.getWorld().getName())).getHandle();
		GameProfile gameProfile = new GameProfile(UUID.randomUUID(),
				ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Sr.Gil");
		EntityPlayer npc = new EntityPlayer(server, world, gameProfile);

		// i wanna save this shit to data.yml:
		npc.setLocation(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(),
				player.getLocation().getYaw(), player.getLocation().getPitch());

		String[] name = getSkin(player, skin);
		gameProfile.getProperties().put("textures", new Property("textures", name[0], name[1]));

		addNPCPacket(npc);
		NPC.add(npc); // para adicionar o NPC à lista.

		// saving to data.yml

		int var = 1;

		if (Main.getData().contains("data"))
			// se diz "data: " no yml eu sei que tenho data la dentro....
			var = Main.getData().getConfigurationSection("data").getKeys(false).size() + 1;

		// casting to int para salvar data.
		Main.getData().set("data." + var + ".x", player.getLocation().getX()); // npc x coord
		Main.getData().set("data." + var + ".y", player.getLocation().getY()); // npc Y coord
		Main.getData().set("data." + var + ".z", player.getLocation().getZ()); // npc Z coord
		Main.getData().set("data." + var + ".p", player.getLocation().getPitch()); // npc Pitch coord
		Main.getData().set("data." + var + ".yaw", player.getLocation().getYaw()); // npc Yaw coord
		Main.getData().set("data." + var + ".world", player.getLocation().getWorld().getName()); // npc name
		Main.getData().set("data." + var + ".name", skin); // npc skin
		Main.getData().set("data." + var + ".text", name[0]); // npc texture stuff 1
		Main.getData().set("data." + var + ".signature", name[1]); // npc texture stuff 2
		Main.saveData(); // ! Saving POGGERS. dont forget to save man.
	}
	
	public static void loadNPC(Location loc, GameProfile profile) {
		MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
		WorldServer world = ((CraftWorld) loc.getWorld()).getHandle();
		EntityPlayer npc = new EntityPlayer(server, world, profile);

		npc.setLocation(loc.getX(), loc.getY(), loc.getZ(),
				loc.getYaw(), loc.getPitch());

		addNPCPacket(npc);
		NPC.add(npc); // para adicionar o NPC à lista.
	}

	private static String[] getSkin(Player player, String name) {
		try {
			URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
			InputStreamReader reader = new InputStreamReader(url.openStream());
			String uuid = new JsonParser().parse(reader).getAsJsonObject().get("id").getAsString();

			URL url2 = new URL(
					"https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
			InputStreamReader reader2 = new InputStreamReader(url2.openStream());
			JsonObject property = new JsonParser().parse(reader2).getAsJsonObject().get("properties").getAsJsonArray()
					.get(0).getAsJsonObject();

			String texture = property.get("value").getAsString();
			String signature = property.get("signature").getAsString();

			return new String[] { texture, signature };

		} catch (Exception e) {
			player.sendMessage(ChatColor.RED + "[DonaDoBar] " + "Não consegui mandar o request correctly.");
			EntityPlayer p = ((CraftPlayer) player).getHandle();
			GameProfile profile = p.getProfile();
			Property property = profile.getProperties().get("textures").iterator().next();
			String texture = property.getValue();
			String signature = property.getSignature();
			return new String[] { texture, signature };

		}
	}

	public static void addNPCPacket(EntityPlayer npc) {
		// mandar a packet do NPC a toda a gente do server.
		for (Player player : Bukkit.getOnlinePlayers()) {

			PlayerConnection connection = ((CraftPlayer) player).getHandle().b;

			connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, npc));
			connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
			connection.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte) (npc.getBukkitYaw() * 256 / 360)));
		}

	}
	
	public static void removeNPC(Player p, EntityPlayer npc) {
		PlayerConnection connection = ((CraftPlayer) p).getHandle().b;
		connection.sendPacket(new PacketPlayOutEntityDestroy(npc.getId())); //removing the packet.
	}

	public static void addJoinPacket(Player player) {
		for (EntityPlayer npc : NPC) {
			PlayerConnection connection = ((CraftPlayer) player).getHandle().b;

			connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, npc));
			connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
			connection.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte) (npc.getBukkitYaw() * 256 / 360)));
		}

	}

	public static List<EntityPlayer> getNPCs() {
		// get from private list
		return NPC;
	}

	public static List<EntityPlayer> deleteNPCs() {
		// get from private list
		//preciso de apagar o que está na file tambem
		Main.delData();
				
		for (Player player : Bukkit.getOnlinePlayers()) {
			PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
			for (EntityPlayer npc : NPC) {
				connection.sendPacket(new PacketPlayOutEntityDestroy(npc.getId())); //removing the packet.
			}
		}
		NPC.clear();
		
		
		
		return NPC;
	}

}
