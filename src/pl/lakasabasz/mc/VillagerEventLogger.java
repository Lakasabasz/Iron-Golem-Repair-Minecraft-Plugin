package pl.lakasabasz.mc;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.raid.RaidTriggerEvent;

import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;

public class VillagerEventLogger implements Listener {

	private String getVillagerIDs(Villager v) {
		if(v.getCustomName() == null) return v.getProfession().toString() + " " + v.getType().toString() + " " + v.getVillagerLevel() + " "  + v.getLocation().toVector().toString();
		return v.getCustomName();
	}
	
	@EventHandler
	public void onEntityDeathEvent(EntityDeathEvent e) {
		if(e.getEntity() instanceof Villager) {
			Villager v = (Villager) e.getEntity();
			Logger.logError("Villager " + getVillagerIDs(v) + " was killed");
			for(Player p : Bukkit.getServer().getOnlinePlayers()) {
				if(p.hasPermission("golemreapir.villagerlog")) {
					p.sendMessage("[GolemRepair] §4Villager " + getVillagerIDs(v) + " was killed");
				}
			}
		}
	}
	
	@EventHandler
	public void onVillagerProfesionChange(VillagerCareerChangeEvent e) {
		Villager v = e.getEntity();
		Logger.logWarning("Villager " + getVillagerIDs(v) + " changed profession on " + e.getProfession().toString() + " because " + e.getReason().toString());
		for(Player p : Bukkit.getServer().getOnlinePlayers()) {
			if(p.hasPermission("golemreapir.villagerlog")) {
				p.sendMessage("[GolemRepair] §6Villager " + getVillagerIDs(v) + " changed profession on " + e.getProfession().toString() + " because " + e.getReason().toString());
			}
		}
	}
	
	@EventHandler
	public void onRadiTriggerEvent(RaidTriggerEvent e) {
		Logger.logWarning(e.getPlayer().getDisplayName() + " triggered raid in location " + e.getRaid().getLocation().getBlockX() + " " + e.getRaid().getLocation().getBlockZ());
		for(Player p : Bukkit.getServer().getOnlinePlayers()) {
			if(p.hasPermission("golemreapir.villagerlog")) {
				p.sendMessage("[GolemRepair] §4" + e.getPlayer().getDisplayName() + " triggered raid in location " + e.getRaid().getLocation().getBlockX() + " " + e.getRaid().getLocation().getBlockZ());
			}
		}
	}
	
	@EventHandler
	public void onEntityBreedEvent(EntityBreedEvent e) {
		if(e.getEntity() instanceof Villager) {
			Logger.logInfo("New villager was born");
		}
		for(Player p : Bukkit.getServer().getOnlinePlayers()) {
			if(p.hasPermission("golemreapir.villagerlog")) {
				p.sendMessage("[GolemRepair] §2New villager was born");
			}
		}
	}
	
	@EventHandler
	public void onEntityRemoved(EntityRemoveFromWorldEvent e) {
		if(e.getEntity() instanceof Villager) {
			Villager v = (Villager) e.getEntity();
			if(v.getVillagerLevel() > 1
					|| v.getCustomName() != null 
					|| v.getKiller() == null) {
				if(!v.getLocation().isChunkLoaded()) {
					v.getChunk().setForceLoaded(true);
					v.getChunk().load();
				}
				Entity ent = Bukkit.getWorld("world").spawnEntity(e.getEntity().getLocation(), e.getEntityType());
				Villager vil = (Villager) ent;
				vil.setAge(v.getAge());
				vil.setCustomName(v.getCustomName());
				vil.setProfession(v.getProfession());
				vil.setRecipes(v.getRecipes());
				vil.setVillagerExperience(v.getVillagerExperience());
				vil.setVillagerLevel(v.getVillagerLevel());
				vil.setVillagerType(v.getVillagerType());
				for(Player p : Bukkit.getServer().getOnlinePlayers()) {
					if(p.hasPermission("golemreapir.villagerlog")) {
						p.sendMessage("[GolemRepair] §3Villager was resurected");
					}
				}
			}
		}
	}
	
}
