package dev.sweetberry.wwizardry;

import dev.sweetberry.wwizardry.content.ContentInitializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mod implements ModInitializer {
    public static final String MODID = "wwizardry";
    public static final Logger LOGGER = LoggerFactory.getLogger("Wandering Wizardry");

    @Override
    public void onInitialize() {
		ContentInitializer.init();
    }

    public static Identifier id(String path) {
        return new Identifier(MODID, path);
    }
}
