package dev.sweetberry.wwizardry.client.content;

import dev.sweetberry.wwizardry.client.content.events.ClientEvents;

public class ClientContentInitializer {
	public static void init() {
		ClientEvents.init();
		RenderLayers.init();
		DatagenRegistryAttachment.init();
	}
}
