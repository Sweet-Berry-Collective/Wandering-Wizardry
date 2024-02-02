package dev.sweetberry.wwizardry.content.net;

import dev.sweetberry.wwizardry.api.net.PacketRegistry;
import dev.sweetberry.wwizardry.content.net.packet.AltarCraftPacket;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class NetworkingInitializer {
	public static void init() {
		PacketRegistry.register(AltarCraftPacket.ID, AltarCraftPacket::new);

		PacketRegistry.SEND_TO_CLIENT.listen((player, packet) -> {
			var payload = PacketByteBufs.create();
			packet.writeTo(payload);
			ServerPlayNetworking.send(player, packet.getId(), payload);
		});
		PacketRegistry.registerTo((id, constructor) -> {
			ServerPlayNetworking.registerGlobalReceiver(id, ((server, player, handler, buf, responseSender) -> {
				var packet = constructor.create(buf);
				packet.onServerReceive(server, player.serverLevel(), player);
			}));
		});
	}
}
