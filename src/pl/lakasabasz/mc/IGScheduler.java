package pl.lakasabasz.mc;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import pl.lakasabasz.mc.Main;

public class IGScheduler implements Runnable {
	
	private void spawnIG(Location l) {
		l.getWorld().spawnEntity(l, EntityType.IRON_GOLEM);
	}
	
	@Override
	public void run() {
		if(Main.spawningData.size() == 0 || Bukkit.getServer().getWorld("world").getPlayers().size() == 0) {
			return;
		}
		for(Location l : Main.spawningData.keySet()) {
			if(!l.getChunk().isLoaded()) continue;
			if(Main.spawningData.get(l) <= 0) {
				spawnIG(l);
				Random r = new Random();
				Main.spawningData.replace(l, r.nextInt(Main.max - Main.min + 1)+Main.min);
			} else {
				Main.spawningData.replace(l, Main.spawningData.get(l)-1);
			}
		}

	}

}
