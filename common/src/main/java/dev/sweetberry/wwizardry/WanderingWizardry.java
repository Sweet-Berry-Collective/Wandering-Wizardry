package dev.sweetberry.wwizardry;

import dev.sweetberry.wwizardry.content.ContentInitializer;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class WanderingWizardry {
	public static final String MODID = "wwizardry";
	public static final Logger LOGGER = LoggerFactory.getLogger("Wandering Wizardry");

	public static Function<String, Boolean> modLoadedCheck;

	public static boolean init = false;
	public static String platform = "unknown";

	public static void init(String platform) {
		if (init)
			return;
		WanderingWizardry.platform = platform;
		init = true;
		WanderingWizardry.LOGGER.info("*tips altar* w'wizardry");
		ContentInitializer.init();
	}

	public static ResourceLocation id(String id) {
		return new ResourceLocation(MODID, id);
	}

	public static boolean isModLoaded(String modid) {
		return modLoadedCheck.apply(modid);
	}
}
