package Polpy.DonaDoBar.npc;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.level.EntityPlayer;

public class Main extends JavaPlugin implements Listener {
	
	public static DataManager data;
	
	
	
	@Override
	public void onEnable() {
		getLogger().info("Live!");
		
		data = new DataManager(this);
		
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		Bukkit.getServer().getPluginManager().registerEvents(new ClickNPC(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new AttackNPC(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new OnDeathListener(), this);
		
		if(data.getConfig().contains("data")) //so dou load quando contem data.
			loadNPC();
		
		
		if(!Bukkit.getOnlinePlayers().isEmpty()) //se no server reload ainda estiver players quero dar inject na mesma.
			for(Player player: Bukkit.getOnlinePlayers()) {
				PacketReader reader = new PacketReader();
				reader.inject(player);
			}
	}
	
	
	@Override
	public void onDisable() {
		getLogger().info("Closed!");
		
		if(!Bukkit.getOnlinePlayers().isEmpty()) //se no server reload ainda estiver players quero dar inject na mesma.
			for(Player player: Bukkit.getOnlinePlayers()) {
				PacketReader reader = new PacketReader();
				reader.uniject(player);
				for(EntityPlayer npc: NPC.getNPCs())
					NPC.removeNPC(player,npc); //delete this packet
			}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {

		
		PacketReader reader = new PacketReader();
		reader.inject(event.getPlayer());
		
		if (NPC.getNPCs() == null)
			return;

		if (NPC.getNPCs().isEmpty())
			return;

		NPC.addJoinPacket(event.getPlayer()); // se não for empty add para todos os npcs para vermos.

		

	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		PacketReader reader = new PacketReader();
		reader.uniject(event.getPlayer());

	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		if (label.equalsIgnoreCase("createsenhora")) {
			if (args.length == 0) {
				NPC.createNPC(player, player.getName()); // passo o proprio nome
				player.sendMessage(ChatColor.ITALIC+ "" +
						ChatColor.AQUA + "[DonaDoBar] "+ ChatColor.RESET + "Não passados argumentos, criado um NPC com a própria skin.");
				return true;
			}
			if (!(sender instanceof Player)) {
				sender.sendMessage("[DonaDoBar] Desculpa bro, só players....");
				return true;
			}
			NPC.createNPC(player, args[0]); // passo o proprio nome
			player.sendMessage(ChatColor.ITALIC+ "" +ChatColor.AQUA + "[DonaDoBar] " + ChatColor.RESET+ "Player criado com a skin do nome: " + args[0]);
			return true;

		} else if (label.equalsIgnoreCase("showallnpcs")) {
			player.sendMessage(ChatColor.ITALIC+ "" +ChatColor.AQUA + "[DonaDoBar] "+ ChatColor.RESET + "Showing all NPCS:");
			for (EntityPlayer npc : NPC.getNPCs()) {
				player.sendMessage(npc.toString());
			}
		} else if (label.equalsIgnoreCase("deleteallnpcs")) {
			NPC.deleteNPCs();
			player.sendMessage(ChatColor.ITALIC+ "" + ChatColor.RED + "[DonaDoBar] "+ ChatColor.BOLD+ "DELETED ALL NPCS");
		}

		return false;
	}
	
	public static FileConfiguration getData() {
		return data.getConfig();
	}
	
	public static void delData() {
		data.deleteConfig();
	}
	
	public static void saveData() {
		data.saveConfig();
	}
	
	public void loadNPC() {
		FileConfiguration file = data.getConfig();
		file.getConfigurationSection("data").getKeys(false).forEach(npc -> {
			Location location = new Location(Bukkit.getWorld(file.getString("data." + npc + ".world")),
					file.getDouble("data."+ npc+ ".x"),
					file.getDouble("data."+ npc+ ".y"),
					file.getDouble("data."+ npc+ ".z")
					); //new Loc
			
			location.setPitch((float) file.getDouble("data."+ npc+ ".p"));
			location.setYaw((float) file.getDouble("data."+ npc+ ".yaw"));
			
			String name = file.getString("data."+npc+ ".name");
			GameProfile gameProfile = new GameProfile(UUID.randomUUID(),
					ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Sr.Gil");
			gameProfile.getProperties().put("textures", new Property("textures",
					file.getString("data."+npc+".text"),
					file.getString("data."+npc+".signature")));
			
			NPC.loadNPC(location, gameProfile);
		});
		
	}
	
}
