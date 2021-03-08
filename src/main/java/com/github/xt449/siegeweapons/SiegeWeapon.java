package com.github.xt449.siegeweapons;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.FallingBlock;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.HashMap;

/**
 * @author Jonathan Talcott (xt449 / BinaryBanana)
 */
public class SiegeWeapon {

	static final HashMap<BlockLocation, SiegeWeapon> map = new HashMap<>();

	final CardinalDirection direction;
	final BlockLocation location;
	final ArmorStand controlsDisplayArmorStand;
	private int controls = 128;

	public SiegeWeapon(CardinalDirection direction, BlockLocation location, ArmorStand controlsDisplayArmorStand) {
		this.direction = direction;
		this.location = location;
		this.controlsDisplayArmorStand = controlsDisplayArmorStand;

		map.put(location, this);
	}

	public void remove() {
		controlsDisplayArmorStand.remove();

		map.remove(location);
	}

	public void destroy() {
		remove();

		location.getBlock().setType(Material.AIR);
	}

	public boolean incrementPower() {
		if((controls & 0b1111) == 0b1111) {
			return false;
		}

		controls += 1;
		return true;
	}

	public boolean decrementPower() {
		if((controls & 0b1111) == 0b0000) {
			return false;
		}

		controls -= 1;
		return true;
	}

	public boolean incrementAngle() {
		if((controls >>> 4) == 0b1111) {
			return false;
		}

		controls += 16;
		return true;
	}

	public boolean decrementAngle() {
		if((controls >>> 4) == 0b0000) {
			return false;
		}

		controls -= 16;
		return true;
	}

	public int getPower() {
		return controls & 0b1111;
	}

	public int getAngle() {
		int angle = (controls >>> 4) - 8;
		return angle < 0 ? angle : angle + 1;
	}

	public void updateControlsDisplay() {
		final Location bottomLeft = location.getLocation().add(DisplayArmorStand.OFFSET_BLOCK_X, DisplayArmorStand.OFFSET_BLOCK_Y, DisplayArmorStand.OFFSET_BLOCK_Z);
		switch(direction) {
			case NORTH:
				controlsDisplayArmorStand.teleport(bottomLeft.add(0, 0, DisplayArmorStand.OFFSET_INTERVAL_MAX).subtract(
						-DisplayArmorStand.OFFSET_INTERVAL_16 * (controls >>> 4),
						0,
						DisplayArmorStand.OFFSET_INTERVAL_16 * (controls & 0b1111)
				));
				break;
			case EAST:
				controlsDisplayArmorStand.teleport(bottomLeft./*no need to offset for primary direction*/add(
						DisplayArmorStand.OFFSET_INTERVAL_16 * (controls & 0b1111),
						0,
						DisplayArmorStand.OFFSET_INTERVAL_16 * (controls >>> 4)
				));
				break;
			case SOUTH:
				controlsDisplayArmorStand.teleport(bottomLeft.add(DisplayArmorStand.OFFSET_INTERVAL_MAX, 0, 0).add(
						-DisplayArmorStand.OFFSET_INTERVAL_16 * (controls >>> 4),
						0,
						DisplayArmorStand.OFFSET_INTERVAL_16 * (controls & 0b1111)
				));
				break;
			case WEST:
				controlsDisplayArmorStand.teleport(bottomLeft.add(DisplayArmorStand.OFFSET_INTERVAL_MAX, 0, DisplayArmorStand.OFFSET_INTERVAL_MAX).subtract(
						DisplayArmorStand.OFFSET_INTERVAL_16 * (controls & 0b1111),
						0,
						DisplayArmorStand.OFFSET_INTERVAL_16 * (controls >>> 4)
				));
				break;
		}
	}

	public void fire() {
		final Block block = location.getBlock();
		final Inventory inventory = ((Container) block.getState()).getInventory();
		final ItemStack[] items = inventory.getContents();

		int weight = 0;
		boolean explosive = false;
		boolean fire = false;

		if(inventory.contains(Material.STONE)) {
			for(int i = 0; i < items.length; i++) {
				if(items[i] == null) {
					continue;
				}
				if(items[i].getType() == Material.STONE) {
					weight += items[i].getAmount();
					items[i].setAmount(0);
				} else if(!explosive && items[i].getType() == Material.TNT) {
					explosive = true;
					items[i].setAmount(items[i].getAmount() - 1);
				} else if(!fire && items[i].getType() == Material.LAVA_BUCKET) {
					fire = true;
					items[i].setType(Material.BUCKET);
				}
			}

			final FallingBlock projectile = block.getWorld().spawnFallingBlock(block.getLocation().add(0.5, 0.5, 0.5), Bukkit.createBlockData(Material.BLACKSTONE));
			projectile.setMetadata(PROJECTILE_WEIGHT_META, new FixedMetadataValue(SiegeWeapons.instance, weight));
			projectile.setMetadata(PROJECTILE_EXPLOSIVE_META, new FixedMetadataValue(SiegeWeapons.instance, explosive));
			projectile.setMetadata(PROJECTILE_FIRE_META, new FixedMetadataValue(SiegeWeapons.instance, fire));
			final Vector velocity = new Vector(direction.modX, 0.5, direction.modZ);

			switch(direction) {
				case NORTH:
					velocity.add(new Vector(getAngle() / 8F, getPower() / 10F, getPower() / -12F));
					break;
				case EAST:
					velocity.add(new Vector(getPower() / 12F, getPower() / 10F, getAngle() / 8F));
					break;
				case SOUTH:
					velocity.add(new Vector(getAngle() / -8F, getPower() / 10F, getPower() / 12F));
					break;
				case WEST:
					velocity.add(new Vector(getPower() / -12F, getPower() / 10F, getAngle() / -8F));
					break;
			}

			projectile.setVelocity(velocity);
			projectile.setDropItem(true);
			projectile.setHurtEntities(true);
		}
	}

	static final String PROJECTILE_WEIGHT_META = "siegeweapons.projectile.weight";
	static final String PROJECTILE_EXPLOSIVE_META = "siegeweapons.projectile.explosive";
	static final String PROJECTILE_FIRE_META = "siegeweapons.projectile.fire";
}
