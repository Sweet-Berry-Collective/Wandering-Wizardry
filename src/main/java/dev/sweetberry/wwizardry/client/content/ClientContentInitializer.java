package dev.sweetberry.wwizardry.client.content;

import dev.sweetberry.wwizardry.client.content.events.ClientEvents;
import dev.sweetberry.wwizardry.client.content.net.ClientNetworkingInitializer;

public class ClientContentInitializer {
	public static void init() {
		ClientEvents.init();
		RenderLayers.init();
		DatagenRegistryAttachment.init();
		ClientNetworkingInitializer.init();
	}
}
