package com.github.xt449.siegeweapons;

import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

/**
 * @author Jonathan Talcott (xt449 / BinaryBanana)
 */
public class DisplayArmorStand {

	private static final float RIGHTARM_X = 75.0f;
	private static final float RIGHTARM_Y = 0.0f;
	private static final float RIGHTARM_Z = -45.0f;
	static final EulerAngle BUTTON_RIGHTARM_POSE = new EulerAngle(RIGHTARM_X * Math.PI / 180, RIGHTARM_Y * Math.PI / 180, RIGHTARM_Z * Math.PI / 180);

	static final double OFFSET_X = -0.4450;
	static final double OFFSET_Y = 0.0000;
	static final double OFFSET_Z = -0.2050;

	static final double OFFSET_X_MAX = -0.0075;
	static final double OFFSET_Y_MAX = 0.4375;
	static final double OFFSET_Z_MAX = 0.2325;

	static final double OFFSET_BLOCK_X = 0.0550;
	static final double OFFSET_BLOCK_Y = 0.5000;
	static final double OFFSET_BLOCK_Z = 0.2950;

	static final double OFFSET_BLOCK_X_MAX = 0.4925;
	static final double OFFSET_BLOCK_Z_MAX = 0.7325;

	static final double OFFSET_INTERVAL_16 = 0.0625;
	static final double OFFSET_INTERVAL_MAX = 0.9375;

	static void simpleSetup(ArmorStand armorStand) {
		armorStand.setRightArmPose(BUTTON_RIGHTARM_POSE);
		armorStand.setBasePlate(false);
		armorStand.setMarker(true);
		armorStand.setSmall(true);
		armorStand.setVisible(false);
		armorStand.setInvulnerable(true);
		armorStand.setSilent(true);
		armorStand.getEquipment().setItemInMainHand(new ItemStack(Material.POLISHED_BLACKSTONE_BUTTON), true);
	}

}
