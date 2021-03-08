package com.github.xt449.siegeweapons;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * @author Jonathan Talcott (xt449 / BinaryBanana)
 */
public final class PlayerListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL)
	private void onPlayerInteract(PlayerInteractEvent event) {
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(event.useInteractedBlock() != Event.Result.DENY) {
				final Block block = event.getClickedBlock();
				if(block != null) {
					if(block.getType() == Material.DISPENSER) {
						final SiegeWeapon siegeWeapon = SiegeWeapon.map.get(BlockLocation.getFromBlock(block));

						if(siegeWeapon != null) {
							if(event.getMaterial() == Material.LEAD) {
								if(event.getBlockFace() == BlockFace.UP) {
									if(siegeWeapon.incrementPower()) {
										block.getWorld().playSound(block.getLocation().add(0.5, 0.5, 0.5), Sound.BLOCK_DISPENSER_FAIL, 1, 1.5F);
										siegeWeapon.updateControlsDisplay();
									} else {
										block.getWorld().playSound(block.getLocation().add(0.5, 0.5, 0.5), Sound.BLOCK_WOODEN_BUTTON_CLICK_OFF, 1, 0F);
									}
									event.setCancelled(true);
								} else {
									final CardinalDirection face = CardinalDirection.getFromBlockFace(event.getBlockFace());

									if(face == siegeWeapon.direction.getOpposite()) {
										if(siegeWeapon.decrementPower()) {
											block.getWorld().playSound(block.getLocation().add(0.5, 0.5, 0.5), Sound.BLOCK_DISPENSER_FAIL, 1, 1.5F);
											siegeWeapon.updateControlsDisplay();
										} else {
											block.getWorld().playSound(block.getLocation().add(0.5, 0.5, 0.5), Sound.BLOCK_WOODEN_BUTTON_CLICK_OFF, 1, 0F);
										}
										event.setCancelled(true);
									} else if(face == siegeWeapon.direction.getLeft()) {
										if(siegeWeapon.decrementAngle()) {
											block.getWorld().playSound(block.getLocation().add(0.5, 0.5, 0.5), Sound.BLOCK_DISPENSER_FAIL, 1, 1.5F);
											siegeWeapon.updateControlsDisplay();
										} else {
											block.getWorld().playSound(block.getLocation().add(0.5, 0.5, 0.5), Sound.BLOCK_WOODEN_BUTTON_CLICK_OFF, 1, 0F);
										}
										event.setCancelled(true);
									} else if(face == siegeWeapon.direction.getRight()) {
										if(siegeWeapon.incrementAngle()) {
											block.getWorld().playSound(block.getLocation().add(0.5, 0.5, 0.5), Sound.BLOCK_DISPENSER_FAIL, 1, 1.5F);
											siegeWeapon.updateControlsDisplay();
										} else {
											block.getWorld().playSound(block.getLocation().add(0.5, 0.5, 0.5), Sound.BLOCK_WOODEN_BUTTON_CLICK_OFF, 1, 0F);
										}
										event.setCancelled(true);
									}
								}
							} else if(event.getMaterial() == Material.LEVER) {
								siegeWeapon.fire();
							}
						}
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	private void onBlockPlace(BlockPlaceEvent event) {
		final Block block = event.getBlock();

		if(block.getType() == Material.DISPENSER) {
			final CardinalDirection direction = CardinalDirection.getFromBlockFace(((Directional) block.getBlockData()).getFacing());
			if(direction == null) {
				return;
			}

			block.getWorld().spawn(block.getLocation(), ArmorStand.class, armorStand -> {
				DisplayArmorStand.simpleSetup(armorStand);

				final BlockLocation blockLocation = BlockLocation.getFromBlock(block);
				final SiegeWeapon siegeWeapon = new SiegeWeapon(direction, blockLocation, armorStand);

				SiegeWeapon.map.put(blockLocation, siegeWeapon);
				siegeWeapon.updateControlsDisplay();
			});
		}
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
