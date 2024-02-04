package dev.sweetberry.wwizardry.fabric.client;

import dev.sweetberry.wwizardry.api.net.PacketRegistry;
import dev.sweetberry.wwizardry.client.WanderingWizardryClient;
import dev.sweetberry.wwizardry.client.content.ClientContentInitializer;
import dev.sweetberry.wwizardry.content.component.BoatComponent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.resources.ResourceLocation;

public class FabricClientInitializer implements ClientModInitializer {
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

		var boatModel = BoatModel.createBodyModel();
		var chestBoatModel = ChestBoatModel.createBodyModel();

		for (var id : BoatComponent.BOATS.keySet()) {
			EntityModelLayerRegistry.registerModelLayer(WanderingWizardryClient.getBoatLayerLocation(id, false), () -> boatModel);
			EntityModelLayerRegistry.registerModelLayer(WanderingWizardryClient.getBoatLayerLocation(id, true), () -> chestBoatModel);
		}
	}
}
