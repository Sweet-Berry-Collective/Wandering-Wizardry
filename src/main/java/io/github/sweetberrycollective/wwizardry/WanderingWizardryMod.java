package io.github.sweetberrycollective.wwizardry;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WanderingWizardryMod implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("Wandering Wizardry");

	@Override
	public void onInitialize(ModContainer mod) {
		LOGGER.info("Hello Quilt world from {}!", mod.metadata().name());
	}
}
