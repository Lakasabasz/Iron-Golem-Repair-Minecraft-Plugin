package pl.lakasabasz.mc;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin {
	
	public static Map<Location, Integer> spawningData = new HashMap<>();
	
	public static int min = 0;
	public static int max = 60;
	
	public static YamlConfiguration fc;
	
	private static JavaPlugin pl;

	
	@Override
	public void onEnable() {
		this.getServer().getConsoleSender().sendMessage("[Golem repair] §e£adowanie pluginu");
		fc = (YamlConfiguration) this.getConfig();
		List<String> data = fc.getStringList("spawnpoints");
		for(String s : data) {
			String[] tmp = s.split(",");
			Double[] nums = new Double[3];
			nums[0] = Double.parseDouble(tmp[1]);
			nums[1] = Double.parseDouble(tmp[2]);
			nums[2] = Double.parseDouble(tmp[3]);
			Location l = new Location(this.getServer().getWorld(tmp[0]), nums[0], nums[1], nums[2]);
			this.getServer().getConsoleSender().sendMessage("[GolemRepair] New location in " + l.toVector().toString());
			Main.spawningData.put(l, 0);
		}
		Main.spawningData = new HashMap<>();
		min = fc.getInt("spawntimes.min");
		max = fc.getInt("spawntimes.max");
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new IGScheduler(), 20*10, 20);
		pl = this;
		@SuppressWarnings("unused")
		Logger l = new Logger();
		this.getServer().getPluginManager().registerEvents(new VillagerEventLogger(), this);
		this.getServer().getConsoleSender().sendMessage("[Golem repair] §eLogger ustawiony " + Logger.getPath());
	}
	
	@Override
	public void onDisable() {
		try {
			fc.save("config.yml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.getServer().getConsoleSender().sendMessage("[Golem repair] off");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!sender.hasPermission("golemreapir.command")) {
			sender.sendMessage("§4Nie masz uprawnieñ");
			return false;
		}
		if(args.length == 0) {
			sender.sendMessage("U¿ycie:");
			sender.sendMessage("/gr set");
			sender.sendMessage("/gr show");
		} else if(args[0].equals("set")) {
			Player p = (Player) sender;
			Location l = p.getLocation();
			String s = l.toString();
			fc.set("spawnpionts", this.getConfig().getStringList("spawnpoints").add(s));
			try {
				fc.save("config.yml");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			spawningData.put(l, 0);
		} else if(args[0].equals("show")) {
			for(Location l : spawningData.keySet()) {
				sender.sendMessage("§2" + l.toString() + " | §3" + spawningData.get(l));
			}
		} else if(args[0].equals("reload")){
			Main.spawningData.clear();
			this.reloadConfig();
			fc = (YamlConfiguration) this.getConfig();
			List<String> data = fc.getStringList("spawnpoints");
			for(String s : data) {
				String[] tmp = s.split(",");
				Double[] nums = new Double[3];
				nums[0] = Double.parseDouble(tmp[1]);
				nums[1] = Double.parseDouble(tmp[2]);
				nums[2] = Double.parseDouble(tmp[3]);
				Location l = new Location(this.getServer().getWorld(tmp[0]), nums[0], nums[1], nums[2]);
				Main.spawningData.put(l, 0);
			}
			Main.spawningData = new HashMap<>();
			min = fc.getInt("spawntimes.min");
			max = fc.getInt("spawntimes.max");
			sender.sendMessage("[Golem Repair] Reloaded");
			this.getServer().getConsoleSender().sendMessage("[Golem Repair] Config reloaded");
		}else {
			sender.sendMessage("§4Say what?!");
			sender.sendMessage(args[0]);
			return false;
		}
		
		return true;
	}
	
	public static JavaPlugin getThisPlugin() {
		return pl;
	}

}
