package dev.sweetberry.wwizardry.fabric.client;

import dev.sweetberry.wwizardry.api.net.PacketRegistry;
import dev.sweetberry.wwizardry.client.WanderingWizardryClient;
import dev.sweetberry.wwizardry.client.content.ClientContentInitializer;
import dev.sweetberry.wwizardry.client.content.RenderLayers;
import dev.sweetberry.wwizardry.client.content.events.ItemTooltipHandler;
import dev.sweetberry.wwizardry.client.content.events.PackReloader;
import dev.sweetberry.wwizardry.content.block.sign.ModdedSignBlock;
import dev.sweetberry.wwizardry.content.component.BoatComponent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.block.Block;

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

		var signModel = SignRenderer.createSignLayer();
		var hangingSignModel = HangingSignRenderer.createHangingSignLayer();

		for (var id : ModdedSignBlock.SIGNS) {
			EntityModelLayerRegistry.registerModelLayer(WanderingWizardryClient.getSignLayerLocation(id, false), () -> signModel);
			EntityModelLayerRegistry.registerModelLayer(WanderingWizardryClient.getSignLayerLocation(id, true), () -> hangingSignModel);
		}

		for (var layer : RenderLayers.LAYERS.entrySet())
			BlockRenderLayerMap.INSTANCE.putBlocks(layer.getKey(), layer.getValue().toArray(Block[]::new));

		ClientTickEvents.END_CLIENT_TICK.register(client -> WanderingWizardryClient.tickCounter++);

		ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new FabricPackReloader());

		ItemTooltipCallback.EVENT.register(ItemTooltipHandler::addTooltips);
	}

	public static class FabricPackReloader extends PackReloader implements IdentifiableResourceReloadListener {
		@Override
		public ResourceLocation getFabricId() {
			return PackReloader.ID;
		}
	}
}
