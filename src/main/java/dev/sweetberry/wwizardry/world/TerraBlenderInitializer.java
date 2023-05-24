package dev.sweetberry.wwizardry.world;

import dev.sweetberry.wwizardry.world.region.OverworldRegion;
import terrablender.api.Regions;
import terrablender.api.TerraBlenderApi;

public class TerraBlenderInitializer implements TerraBlenderApi {
	@Override
	public void onTerraBlenderInitialized() {
		Regions.register(OverworldRegion.INSTANCE);
	}
}
