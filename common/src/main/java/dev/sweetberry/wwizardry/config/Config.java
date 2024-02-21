package dev.sweetberry.wwizardry.config;

import dev.sweetberry.wwizardry.api.config.ConfigHelper;

// TODO: Make it per-world.
public class Config {
	public static final String name = "wwizardry.json";
	private static final Config globalInstance = ConfigHelper.loadGlobalConfig(new Config(), name);

	private double altarSpreadMultiplier = 1;

	private boolean allowOpEnchants = false;

	public static double getAltarSpreadMultiplier() {
		return globalInstance.altarSpreadMultiplier;
	}

	public static void setAltarSpreadMultiplier(double altarSpreadMultiplier) {
		globalInstance.altarSpreadMultiplier = altarSpreadMultiplier;
		ConfigHelper.saveGlobalConfig(globalInstance, name);
	}

	public static boolean getAllowOpEnchants() {
		return globalInstance.allowOpEnchants;
	}

	public static void setAllowOpEnchants(boolean allowOpEnchants) {
		globalInstance.allowOpEnchants = allowOpEnchants;
		ConfigHelper.saveGlobalConfig(globalInstance, name);
	}
}
