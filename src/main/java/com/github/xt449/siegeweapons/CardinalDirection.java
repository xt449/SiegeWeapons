package com.github.xt449.siegeweapons;

import org.bukkit.block.BlockFace;

/**
 * @author Jonathan Talcott (xt449 / BinaryBanana)
 */
public enum CardinalDirection {
	NORTH(0, -1),
	EAST(1, 0),
	SOUTH(0, 1),
	WEST(-1, 0);

	final int modX;
	final int modZ;

	CardinalDirection(int x, int z) {
		modX = x;
		modZ = z;
	}

	public CardinalDirection getOpposite() {
		switch(this) {
			case NORTH:
				return SOUTH;
			case EAST:
				return WEST;
			case SOUTH:
				return NORTH;
			case WEST:
				return EAST;
		}
		return this;
	}

	public CardinalDirection getRight() {
		switch(this) {
			case NORTH:
				return EAST;
			case EAST:
				return SOUTH;
			case SOUTH:
				return WEST;
			case WEST:
				return NORTH;
		}
		return this;
	}

	public CardinalDirection getLeft() {
		switch(this) {
			case NORTH:
				return WEST;
			case EAST:
				return NORTH;
			case SOUTH:
				return EAST;
			case WEST:
				return SOUTH;
		}
		return this;
	}

	public static CardinalDirection getFromBlockFace(BlockFace face) {
		switch(face) {
			case NORTH:
				return NORTH;
			case EAST:
				return EAST;
			case SOUTH:
				return SOUTH;
			case WEST:
				return WEST;
		}

		return null;
	}
}
