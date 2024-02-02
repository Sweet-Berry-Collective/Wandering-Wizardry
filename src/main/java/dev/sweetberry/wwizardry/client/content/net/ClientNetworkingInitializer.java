package dev.sweetberry.wwizardry.client.content.net;

import dev.sweetberry.wwizardry.api.net.PacketRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;

public class ClientNetworkingInitializer {
	public static void init() {
		PacketRegistry.SEND_TO_SERVER.listen(packet -> {
			var payload = PacketByteBufs.create();
			packet.writeTo(payload);
			ClientPlayNetworking.send(packet.getId(), payload);
		});
		PacketRegistry.registerTo((id, constructor) -> {
			ClientPlayNetworking.registerGlobalReceiver(id, ((client, handler, buf, responseSender) -> {
				var packet = constructor.create(buf);
				packet.onClientReceive(client, client.level, client.player);
			}));
		});
	}
}
