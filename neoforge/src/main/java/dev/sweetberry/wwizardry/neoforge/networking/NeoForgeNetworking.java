package dev.sweetberry.wwizardry.neoforge.networking;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.api.net.CustomPacket;
import dev.sweetberry.wwizardry.api.net.PacketRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.ClientPayloadContext;

public class NeoForgeNetworking {

	public static void init(IEventBus bus) {
		bus.addListener(NeoForgeNetworking::register);
	}

	@SubscribeEvent
	public static void register(RegisterPayloadHandlersEvent event) {
		var registrar = event.registrar(WanderingWizardry.MODID);

		PacketRegistry.registerTo((id, codec) -> {
			registrar.playBidirectional(id, codec, (packet, context) -> {
				if (context instanceof ClientPayloadContext) {
					NeoforgeNetworkingClient.handleClient(packet);
				} else {
					var player = (ServerPlayer) context.player();
					packet.onServerReceive(player.server, player.serverLevel(), player);
				}
			});
		});

		PacketRegistry.SEND_TO_SERVER.listen(PacketDistributor::sendToServer);

		PacketRegistry.SEND_TO_CLIENT.listen(PacketDistributor::sendToPlayer);
	}
}
