package com.github.xt449.siegeweapons;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.Objects;

/**
 * @author Jonathan Talcott (xt449 / BinaryBanana)
 */
final class BlockLocation {

	final World world;
	final int x;
	final int y;
	final int z;

	BlockLocation(World world, int x, int y, int z) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public boolean equals(Object other) {
		if(this == other) return true;
		if(other == null || getClass() != other.getClass()) return false;
		BlockLocation that = (BlockLocation) other;
		return x == that.x && y == that.y && z == that.z;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y, z);
	}

	Location getLocation() {
		return new Location(world, x, y, z);
	}

	Block getBlock() {
		return world.getBlockAt(x, y, z);
	}

	static BlockLocation getFromBlock(Block block) {
		return new BlockLocation(block.getWorld(), block.getX(), block.getY(), block.getZ());
	}
}
