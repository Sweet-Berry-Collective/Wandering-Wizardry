package dev.sweetberry.wwizardry.content.net;

import dev.sweetberry.wwizardry.api.net.PacketRegistry;
import dev.sweetberry.wwizardry.content.net.packet.AltarCraftPacket;
import dev.sweetberry.wwizardry.content.net.packet.ComponentSyncPacket;

public class NetworkingInitializer {
	public static void init() {
		PacketRegistry.register(AltarCraftPacket.TYPE, AltarCraftPacket.CODEC);
		PacketRegistry.register(ComponentSyncPacket.TYPE, ComponentSyncPacket.CODEC);
	}
}
