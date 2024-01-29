package dev.sweetberry.wwizardry.client;

import dev.sweetberry.wwizardry.client.content.ClientContentInitializer;
import net.fabricmc.api.ClientModInitializer;

public class ModClient implements ClientModInitializer {
	public static int tickCounter = 0;
	public static int useItemTick = -1;

	@Override
	public void onInitializeClient() {
		ClientContentInitializer.init();
	}
}
