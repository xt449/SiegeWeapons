package com.github.xt449.siegeweapons;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

/**
 * @author Jonathan Talcott (xt449 / BinaryBanana)
 */
public class BlockListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	private void onEntityChangeBlock(EntityChangeBlockEvent event) {
		if(event.getEntityType() == EntityType.FALLING_BLOCK) {
			final Entity projectile = event.getEntity();
			final List<MetadataValue> weightMeta = projectile.getMetadata("siegeweapons.projectile.weight");

			if(weightMeta.size() > 0) {
				event.setCancelled(true);

				boolean explosive = projectile.getMetadata("siegeweapons.projecile.explosive").get(0).asBoolean();
				boolean fire = projectile.getMetadata("siegeweapons.projecile.fire").get(0).asBoolean();

				final Block center = projectile.getLocation().getBlock();
				center.breakNaturally();
				center.getRelative(BlockFace.UP).breakNaturally();
				center.getRelative(BlockFace.DOWN).breakNaturally();
				center.getRelative(BlockFace.NORTH).breakNaturally();
				center.getRelative(BlockFace.EAST).breakNaturally();
				center.getRelative(BlockFace.SOUTH).breakNaturally();
				center.getRelative(BlockFace.WEST).breakNaturally();
				projectile.getWorld().createExplosion(projectile.getLocation(), explosive ? 4F : 1F, fire, true);
			}
		}
	}
}
