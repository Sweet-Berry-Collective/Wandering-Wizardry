package dev.sweetberry.wwizardry.fabric.compat.terrablender;

import dev.sweetberry.wwizardry.compat.terrablender.TerraBlenderInitializer;
import terrablender.api.TerraBlenderApi;

public class FabricTerraBlenderInitializer implements TerraBlenderApi {
	@Override
	public void onTerraBlenderInitialized() {
		TerraBlenderInitializer.init();
	}
}
