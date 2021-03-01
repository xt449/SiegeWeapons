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
		Bukkit.broadcastMessage("Siege Weapon removed!");

		controlsDisplayArmorStand.remove();

		map.remove(location);
	}

	public void destroy() {
		Bukkit.broadcastMessage("Siege Weapon destroyed!");

		remove();

		location.getBlock().setType(Material.AIR);
	}

	public boolean incrementPower() {
		Bukkit.broadcastMessage("incrementPower");
		Bukkit.broadcastMessage("-> " + (controls & 0b1111));
		if((controls & 0b1111) == 0b1111) {
			return false;
		}

		controls += 1;
		Bukkit.broadcastMessage("-> " + (controls & 0b1111));
		return true;
	}

	public boolean decrementPower() {
		Bukkit.broadcastMessage("decrementPower");
		Bukkit.broadcastMessage("-> " + (controls & 0b1111));
		if((controls & 0b1111) == 0b0000) {
			return false;
		}

		controls -= 1;
		Bukkit.broadcastMessage("-> " + (controls & 0b1111));
		return true;
	}

	public boolean incrementAngle() {
		Bukkit.broadcastMessage("incrementAngle");
		Bukkit.broadcastMessage("-> " + (controls >>> 4));
		if((controls >>> 4) == 0b1111) {
			return false;
		}

		controls += 16;
		Bukkit.broadcastMessage("-> " + (controls >>> 4));
		return true;
	}

	public boolean decrementAngle() {
		Bukkit.broadcastMessage("decrementAngle");
		Bukkit.broadcastMessage("-> " + (controls >>> 4));
		if((controls >>> 4) == 0b0000) {
			return false;
		}

		controls -= 16;
		Bukkit.broadcastMessage("-> " + (controls >>> 4));
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
		Bukkit.broadcastMessage("Power: " + getPower() + " Angle: " + (controls >>> 4));

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
		projectile.setMetadata("siegeweapons.projecile.weight", new FixedMetadataValue(SiegeWeapons.instance, weight));
		projectile.setMetadata("siegeweapons.projecile.explosive", new FixedMetadataValue(SiegeWeapons.instance, explosive));
		projectile.setMetadata("siegeweapons.projecile.fire", new FixedMetadataValue(SiegeWeapons.instance, fire));
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

		Bukkit.broadcastMessage("weight: " + weight + "\nexplosive: " + explosive + "\nfire: " + fire);

		projectile.setVelocity(velocity);
		projectile.setDropItem(false);
		projectile.setHurtEntities(true);
	}
}
