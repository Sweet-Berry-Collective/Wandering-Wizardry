package dev.sweetberry.wwizardry.fabric.client;

import dev.sweetberry.wwizardry.client.WanderingWizardryClient;
import dev.sweetberry.wwizardry.client.content.ClientContentInitializer;
import net.fabricmc.api.ClientModInitializer;

public class FabricClientInitializer implements ClientModInitializer {
	public static int tickCounter = 0;
	public static int useItemTick = -1;

	@Override
	public void onInitializeClient() {
		WanderingWizardryClient.init();
		ClientContentInitializer.init();
	}
}
