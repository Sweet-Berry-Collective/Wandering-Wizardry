package dev.sweetberry.wwizardry.neoforge.networking;

import dev.sweetberry.wwizardry.api.net.CustomPacket;
import net.minecraft.client.Minecraft;

public class NeoforgeNetworkingClient {
	public static void handleClient(CustomPacket packet) {
		var client = Minecraft.getInstance();
		packet.onClientReceive(client, client.level, client.player);
	}
}
