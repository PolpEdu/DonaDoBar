package Polpy.DonaDoBar.npc;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import net.md_5.bungee.api.ChatColor;


public class ClickNPC implements Listener{
	String[] frases = {
			"E a baguete é com o quê?",
			"Queria já não quer?",
			"Frango ou Franga?",
			"Mais alguma coisa?",
			"O queijo é derretido (Baguete de bacon)",
			"Baguete de omelete com ovo?",
			"Pode meter o cartão...",
			"Por acaso não conhece um rapaz chamado Hugo? Ele deixou cá o cartão...",
			};
	int vezescontadas, easterEGG, rnd;
	private int id;
	
	@EventHandler
	public void onClick(RightClickNPC event) { //o custum event que criei com os packets.
		Player player = event.getPlayer();
		Random rand = new Random();
		vezescontadas++;
		rnd = rand.nextInt(frases.length);
		
		
		
		if(vezescontadas%2 == 0)
		{
			/*EASTEREGGPART*/
			easterEGG = rand.nextInt(45000);
			if(easterEGG == 42069) {
				player.sendMessage(ChatColor.AQUA+ "[DEI] Sr.Gil: " +ChatColor.YELLOW  + "Olá menino, conseguiste acertar no teu numero da sorte hoje,"
						+ " eu como Sr.Gil da cantina quero te mostrar um sitio especial a meninos como tu que têm muita muita sorte :)))))");
		        
				final int[] i = {0};
				id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), () -> {
		            if(i[0] > 2) {
		            	player.sendMessage(ChatColor.AQUA+ "[DEI] Sr.Gil: "+ChatColor.YELLOW + "Fala comigo depois das aulas no estacionamento do departamento. Tenho uma surpresa para ti :)");		
		            	Bukkit.getScheduler().cancelTask(id);
		            	
		                return;
		            } else
		                i[0]++;
		            
		        }, 0L, 20L);
				
				
			}/*EASTEREGGPART*/
			else {
				player.sendMessage(ChatColor.AQUA+ "[DEI] Sr.Gil: " +ChatColor.RESET +frases[rnd]);
			}
		}
		
		
	}

}

