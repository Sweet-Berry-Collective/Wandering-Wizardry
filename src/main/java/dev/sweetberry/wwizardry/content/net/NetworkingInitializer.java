package dev.sweetberry.wwizardry.content.net;

import dev.sweetberry.wwizardry.content.net.packet.VoidBagPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class NetworkingInitializer {
	public static void init() {
		ServerPlayNetworking.registerGlobalReceiver(VoidBagPayload.TYPE, VoidBagPayload::accept);
	}
}
