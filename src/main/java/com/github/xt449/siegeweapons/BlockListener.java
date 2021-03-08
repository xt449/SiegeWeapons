package com.github.xt449.siegeweapons;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * @author Jonathan Talcott (xt449 / BinaryBanana)
 */
public final class BlockListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	private void onBlockPlace(BlockPlaceEvent event) {
		final Block block = event.getBlock();

		if(block.getType() == Material.DISPENSER) {
			final CardinalDirection direction = CardinalDirection.getFromBlockFace(((Directional) block.getBlockData()).getFacing());
			if(direction == null) {
				return;
			}
			final boolean valid = checkStructure(block, direction);
			Bukkit.broadcastMessage("Valid siege weapon?: " + valid);

			if(valid) {
				block.getWorld().spawn(block.getLocation(), ArmorStand.class, armorStand -> {
					DisplayArmorStand.simpleSetup(armorStand);

					final BlockLocation blockLocation = BlockLocation.getFromBlock(block);
					final SiegeWeapon siegeWeapon = new SiegeWeapon(direction, blockLocation, armorStand);

					siegeWeapon.updateControlsDisplay();
				});
			}
		}
	}

	private boolean checkStructure(Block dispenser, CardinalDirection direction) {
		if(Tag.LOGS.isTagged(dispenser.getRelative(BlockFace.DOWN).getType())) {
			if(!dispenser.getRelative(1, 0, 0).isEmpty()) {
				return false;
			}
			if(!dispenser.getRelative(-1, 0, 0).isEmpty()) {
				return false;
			}
			if(!dispenser.getRelative(0, 0, 1).isEmpty()) {
				return false;
			}
			if(!dispenser.getRelative(0, 0, -1).isEmpty()) {
				return false;
			}

			if(!Tag.LOGS.isTagged(dispenser.getRelative(1, 0, 1).getType())) {
				return false;
			}
			if(!Tag.LOGS.isTagged(dispenser.getRelative(-1, 0, 1).getType())) {
				return false;
			}
			if(!Tag.LOGS.isTagged(dispenser.getRelative(1, 0, -1).getType())) {
				return false;
			}
			if(!Tag.LOGS.isTagged(dispenser.getRelative(-1, 0, -1).getType())) {
				return false;
			}

//			if(dispenser.getRelative(direction.getLeft().toBlockFace()).isEmpty() && dispenser.getRelative(direction.getRight().toBlockFace()).isEmpty())

			return true;
		}

		return false;
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	private void onBlockBreak(BlockBreakEvent event) {
		final Block block = event.getBlock();

		if(block.getType() == Material.DISPENSER) {
			if(block.getType() == Material.DISPENSER) {
				final SiegeWeapon siegeWeapon = SiegeWeapon.map.get(BlockLocation.getFromBlock(block));

				if(siegeWeapon != null) {
					siegeWeapon.remove();
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	private void onBlockExplode(BlockExplodeEvent event) {
		final Block block = event.getBlock();

		if(block.getType() == Material.DISPENSER) {
			if(block.getType() == Material.DISPENSER) {
				final SiegeWeapon siegeWeapon = SiegeWeapon.map.get(BlockLocation.getFromBlock(block));

				if(siegeWeapon != null) {
					siegeWeapon.remove();
				}
			}
		}
	}
}
