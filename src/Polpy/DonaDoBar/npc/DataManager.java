package Polpy.DonaDoBar.npc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class DataManager {
	private Main plugin;
	private FileConfiguration dataConfig = null;
	private File configFile = null;
	
	public DataManager(Main plugin) {
		this.plugin = plugin;
		//save/inicia a config
		saveDefaultConfig();
	}
	
	public void reloadConfig() {
		if(this.configFile == null) {
			this.configFile = new File(this.plugin.getDataFolder(), "data.yml");
		}
		
		this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);
		
		InputStream defaultStream = this.plugin.getResource("data.yml");
		if(defaultStream != null) {
			YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
			this.dataConfig.setDefaults(defaultConfig);
		}
	}
	
	public FileConfiguration getConfig() {
		if(this.dataConfig == null)
			reloadConfig();
		
		return this.dataConfig;
	}
	
	public void saveConfig() {
		if(this.dataConfig == null || this.configFile == null) {
			//vazio
			return;
		}
		
		try {
			this.getConfig().save(this.configFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			plugin.getLogger().log(Level.SEVERE, "Não consegui salvar a config para a file "+ this.configFile, e);
		}
	}
	public void deleteConfig() {
		if(this.dataConfig == null || this.configFile == null) {
			//vazio
			return;
		}
		FileWriter f;
		try {
			//System.out.println(this.plugin.getDataFolder());
			f = new FileWriter(this.plugin.getDataFolder()+"\\data.yml"); //encontro o seu path
			f.write(""); //escrevo nada para a file
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	public void saveDefaultConfig() {
		if(this.configFile == null) {
			this.configFile = new File(this.plugin.getDataFolder(), "data.yml"); 
		}
		
		if(!this.configFile.exists()) {
			this.plugin.saveResource("data.yml", false);
		}
		
	}
	
	
	
}
