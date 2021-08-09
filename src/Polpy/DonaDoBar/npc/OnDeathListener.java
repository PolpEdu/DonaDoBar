package Polpy.DonaDoBar.npc;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.level.EntityPlayer;

public class OnDeathListener implements Listener {
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		
		
		Player player = (Player) e.getEntity();
		//System.out.println(player.getName());
		
		if(e.getDeathMessage().contains("died")) {
			e.setDeathMessage(ChatColor.AQUA+ "[DEI] Sr.Gil: "+ ChatColor.RESET + "Toma-lá que já almoçaste " + player.getName() + ". Querias porrada? Comeste mas é esta sandes de murros...");
		}
	}
}
