package com.github.xt449.siegeweapons;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Jonathan Talcott (xt449 / BinaryBanana)
 */
public final class SiegeWeapons extends JavaPlugin {

	static SiegeWeapons instance;

	public SiegeWeapons() {
		instance = this;
	}

	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
		Bukkit.getPluginManager().registerEvents(new EntityListener(), this);
	}

	@Override
	public void onDisable() {
		SiegeWeapon.map.values().iterator().forEachRemaining(SiegeWeapon::remove);
	}
}
