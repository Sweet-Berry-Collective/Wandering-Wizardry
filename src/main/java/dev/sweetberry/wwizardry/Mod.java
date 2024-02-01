package dev.sweetberry.wwizardry;

import dev.sweetberry.wwizardry.content.ContentInitializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mod implements ModInitializer {
    public static final String MODID = "wwizardry";
    public static final Logger LOGGER = LoggerFactory.getLogger("Wandering Wizardry");

    @Override
    public void onInitialize() {
		LOGGER.info("*tips altar* w'wizardry");
		ContentInitializer.init();
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MODID, path);
    }
}
