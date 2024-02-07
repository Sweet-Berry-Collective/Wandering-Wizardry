package dev.sweetberry.wwizardry.content.gamerule;

import dev.sweetberry.wwizardry.api.config.ConfigHelper;

// TODO: Make it per-world.
public class GameruleInitializer {
	public static final String name = "wwizardry.json";
	private static final GameruleInitializer globalInstance = ConfigHelper.loadGlobalConfig(new GameruleInitializer(), name);

	private double altarSpreadMultiplier = 1;

	public static double getAltarSpreadMultiplier() {
		return globalInstance.altarSpreadMultiplier;
	}

	public static void setAltarSpreadMultiplier(double altarSpreadMultiplier) {
		globalInstance.altarSpreadMultiplier = altarSpreadMultiplier;
		ConfigHelper.loadGlobalConfig(globalInstance, name);
	}
}
