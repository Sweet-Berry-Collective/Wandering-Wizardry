package dev.sweetberry.wwizardry.content.net;

import dev.sweetberry.wwizardry.api.net.PacketRegistry;
import dev.sweetberry.wwizardry.content.net.packet.AltarCraftPacket;

public class NetworkingInitializer {
	public static void init() {
		PacketRegistry.register(AltarCraftPacket.ID, AltarCraftPacket::new);
	}
}
