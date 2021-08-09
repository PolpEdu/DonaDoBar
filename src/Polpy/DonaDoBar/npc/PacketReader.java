package Polpy.DonaDoBar.npc;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayInUseEntity;
import net.minecraft.server.level.EntityPlayer;

public class PacketReader {
	Channel channel;
	public static Map<UUID, Channel> channels = new HashMap<UUID, Channel>();
	//tem que ser HashMaps para dar com varios NPCS
	
	public void inject(Player player) {
		CraftPlayer craftPlayer = (CraftPlayer) player;
		channel = craftPlayer.getHandle().b.a.k;
		channels.put(player.getUniqueId(), channel);
		
		if(channel.pipeline().get("PackeyInjector") != null) {
			return;
		}
		
		//! linha importante, reduz o lag so verificar este packet especifico.
		channel.pipeline().addAfter("decoder", "PacketInjector", new MessageToMessageDecoder<PacketPlayInUseEntity>() {

			
			@Override
			protected void decode(ChannelHandlerContext channel, PacketPlayInUseEntity packet, List<Object> arg) throws Exception {
				//System.out.println("PACKET: "+ packet);
				
				arg.add(packet); 
				
				readPacket(player,packet);
			}
			
		});
	}
	
	public void uniject(Player player) {
		channel = channels.get(player.getUniqueId());
		if (channel.pipeline().get("PacketInjector") != null) //há um channel
			channel.pipeline().remove("PacketInjector");
	}
	
	
	public void readPacket(Player p, Packet<?> packet) { //Passing any packet.
		
		if (packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInUseEntity")) { //ver se o packet mandado é uma interação
			//System.out.println("Interagiram com o SR.GIL! (poggers moment)");
			
			
			int id = (int) getValue(packet, "a"); // a returns de npc
			//System.out.println(id);
			
			if(getValue(packet, "b").toString().split("@",2)[0].equalsIgnoreCase("net.minecraft.network.protocol.game.PacketPlayInUseEntity$1"))
			{
				//Sr. Gil acabou de receber um murro,
				//System.out.println("ATTACK!");
				for (EntityPlayer npc : NPC.getNPCs()) {
					if(npc.getId() == id) {
						
						//Mas não podemos fazer desta forma ^^^^^^ temos que fazer dentro de uma schedule porque assim 2 threads corriam ao mesmo tempo e assim não:
						Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								Bukkit.getPluginManager().callEvent(new AttackClickNPC(p, npc));
							}
							
						}, 0); //para correr instantâneamente.
					
					
					}
				}
				return;
			}
			
			else if(getValue(packet, "b").toString().split("@",2)[0].equalsIgnoreCase("net.minecraft.network.protocol.game.PacketPlayInUseEntity$e"))
			{
				//System.out.println("E packet.");
				return;
			}
		
			else if(getValue(packet, "b").toString().split("@",2)[0].equalsIgnoreCase("net.minecraft.network.protocol.game.PacketPlayInUseEntity$d"))
			{
				//System.out.println("FRASE:");
				for (EntityPlayer npc : NPC.getNPCs()) {
					if(npc.getId() == id) {
						//Bukkit.getPluginManager().callEvent(new RightClickNPC(p, npc)); //chamei o evento que criei...
						//Vi que ele aconteceu pelos packets.
						
						//Mas não podemos fazer desta forma ^^^^^^ temos que fazer dentro de uma schedule porque assim 2 threads corriam ao mesmo tempo e assim não:
						Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								//System.out.println("whatsup");
								Bukkit.getPluginManager().callEvent(new RightClickNPC(p, npc));
							}
							
						}, 0); //para correr instantâneamente.
					
					
					}
				}
			}
			
				
		}
	}
	
	private Object getValue(Object instance, String name) {
		Object result = null;
        try {
        	//System.out.println("INSTANCE: " + instance.toString());
            Field field = instance.getClass().getDeclaredField(name);
            
            //System.out.println("FIELD: " + field.toString());
            
            field.setAccessible(true);
            result = field.get(instance);
            field.setAccessible(false);
            
            //System.out.println("RES: " + result.toString());

        } catch (Exception e) {

            e.printStackTrace();

        }
        
        
        return result;
    }
}
