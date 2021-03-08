package com.github.xt449.siegeweapons;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
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
							event.setCancelled(true);

							if(event.getMaterial() == Material.LEAD) {
								if(event.getBlockFace() == BlockFace.UP) {
									if(siegeWeapon.incrementPower()) {
										block.getWorld().playSound(block.getLocation().add(0.5, 0.5, 0.5), Sound.BLOCK_DISPENSER_FAIL, 1, 1.5F);
										siegeWeapon.updateControlsDisplay();
									} else {
										block.getWorld().playSound(block.getLocation().add(0.5, 0.5, 0.5), Sound.BLOCK_WOODEN_BUTTON_CLICK_OFF, 1, 0F);
									}
								} else {
									final CardinalDirection face = CardinalDirection.getFromBlockFace(event.getBlockFace());

									if(face == siegeWeapon.direction.getOpposite()) {
										if(siegeWeapon.decrementPower()) {
											block.getWorld().playSound(block.getLocation().add(0.5, 0.5, 0.5), Sound.BLOCK_DISPENSER_FAIL, 1, 1.5F);
											siegeWeapon.updateControlsDisplay();
										} else {
											block.getWorld().playSound(block.getLocation().add(0.5, 0.5, 0.5), Sound.BLOCK_WOODEN_BUTTON_CLICK_OFF, 1, 0F);
										}
									} else if(face == siegeWeapon.direction.getLeft()) {
										if(siegeWeapon.decrementAngle()) {
											block.getWorld().playSound(block.getLocation().add(0.5, 0.5, 0.5), Sound.BLOCK_DISPENSER_FAIL, 1, 1.5F);
											siegeWeapon.updateControlsDisplay();
										} else {
											block.getWorld().playSound(block.getLocation().add(0.5, 0.5, 0.5), Sound.BLOCK_WOODEN_BUTTON_CLICK_OFF, 1, 0F);
										}
									} else if(face == siegeWeapon.direction.getRight()) {
										if(siegeWeapon.incrementAngle()) {
											block.getWorld().playSound(block.getLocation().add(0.5, 0.5, 0.5), Sound.BLOCK_DISPENSER_FAIL, 1, 1.5F);
											siegeWeapon.updateControlsDisplay();
										} else {
											block.getWorld().playSound(block.getLocation().add(0.5, 0.5, 0.5), Sound.BLOCK_WOODEN_BUTTON_CLICK_OFF, 1, 0F);
										}
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
}
