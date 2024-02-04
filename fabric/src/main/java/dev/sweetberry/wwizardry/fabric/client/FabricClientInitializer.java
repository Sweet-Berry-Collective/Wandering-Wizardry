package dev.sweetberry.wwizardry.fabric.client;

import dev.sweetberry.wwizardry.api.net.PacketRegistry;
import dev.sweetberry.wwizardry.client.WanderingWizardryClient;
import dev.sweetberry.wwizardry.client.content.ClientContentInitializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;

public class FabricClientInitializer implements ClientModInitializer {
	public static int tickCounter = 0;
	public static int useItemTick = -1;

	@Override
	public void onInitializeClient() {
		WanderingWizardryClient.init();
		ClientContentInitializer.init();
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
