package dev.sweetberry.wwizardry.content.net;

import dev.sweetberry.wwizardry.content.net.packet.VoidBagPayload;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

public class NetworkingInitializer {
	public static void init() {
		ServerPlayNetworking.registerGlobalReceiver(VoidBagPayload.ID, VoidBagPayload::accept);
	}
}
