package dev.sweetberry.wwizardry.fabric;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.content.ContentInitializer;
import net.fabricmc.api.ModInitializer;

public class FabricInitializer implements ModInitializer {
    @Override
    public void onInitialize() {
		WanderingWizardry.init();
		ContentInitializer.init();
    }
}
