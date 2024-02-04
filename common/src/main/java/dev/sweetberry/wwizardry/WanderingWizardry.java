package dev.sweetberry.wwizardry;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WanderingWizardry {
	public static final String MODID = "wwizardry";
	public static final Logger LOGGER = LoggerFactory.getLogger("Wandering Wizardry");

	public static void init() {
		WanderingWizardry.LOGGER.info("*tips altar* w'wizardry");
		// TODO!
	}

	public static ResourceLocation id(String id) {
		return new ResourceLocation(MODID, id);
	}
}
