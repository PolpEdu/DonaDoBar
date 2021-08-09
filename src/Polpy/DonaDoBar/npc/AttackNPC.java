package Polpy.DonaDoBar.npc;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import net.md_5.bungee.api.ChatColor;

public class AttackNPC implements Listener{
	Random rand = new Random();
	int vezesbatidas =0;
	@EventHandler
	public void onClick(AttackClickNPC event) { //o custom event que criei com os packets.
		Player player = event.getPlayer();
		vezesbatidas++;
		if(vezesbatidas >3) {
			player.damage(rand.nextDouble()+ rand.nextInt(5));
			player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, vezesbatidas*70, 1));
			
			if(vezesbatidas >10 ) {
				vezesbatidas = 0;
				player.sendMessage(ChatColor.AQUA+ "[DEI] Sr.Gil: "+ ChatColor.RED +"Já te disse que não quero brincadeiras, amigo....");
				player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 800, 1));
				player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 1000, 1));
			}
			else if(vezesbatidas == 7) {
				player.addPotionEffect(new PotionEffect(PotionEffectType.UNLUCK, 500, 1));
			}
			else {
				player.sendMessage(ChatColor.AQUA+ "[DEI] Sr.Gil: "+ ChatColor.RED +"Anda cá oh cabrão");
			}
		}
		else {
			switch(vezesbatidas) {
				case 1:
					player.sendMessage(ChatColor.AQUA+ "[DEI] Sr.Gil: "+ ChatColor.RED +"Oh crl queres apanhar???"
							+ " Só por causa dessa merda a tua sandes vem com ovos podres fdp");
					break;
				case 2:
					player.sendMessage(ChatColor.AQUA+ "[DEI] Sr.Gil: "+ ChatColor.RED +"Já disse para estares quieto fdp....");
					break;
				case 3:
					player.sendMessage(ChatColor.AQUA+ "[DEI] Sr.Gil: "+ ChatColor.RED +"Tou me a passar... Deixa-me ir ai deixa...");
					break;
					
			}
		}
	}

}