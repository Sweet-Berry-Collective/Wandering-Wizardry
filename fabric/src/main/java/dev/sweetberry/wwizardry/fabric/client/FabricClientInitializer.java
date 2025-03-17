package dev.sweetberry.wwizardry.fabric.client;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.api.net.PacketRegistry;
import dev.sweetberry.wwizardry.client.WanderingWizardryClient;
import dev.sweetberry.wwizardry.client.content.RenderLayers;
import dev.sweetberry.wwizardry.client.content.events.ClientEvents;
import dev.sweetberry.wwizardry.client.content.events.ItemTooltipHandler;
import dev.sweetberry.wwizardry.client.content.events.PackReloader;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.function.Supplier;

public class FabricClientInitializer implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		WanderingWizardryClient.init();
		ClientEvents.registerBlockEntityRenderers((type, renderer) -> {
			BlockEntityRenderers.register(type.get(), renderer);
		});
		ClientEvents.registerEntityRenderers((type, renderer) -> {
			EntityRendererRegistry.register(type.get(), renderer);
		});

		ClientEvents.registerModelPredicates((item, name, callback) -> {
			ItemProperties.registerGeneric(
				WanderingWizardry.id(name),
				callback
			);
		});

		PacketRegistry.SEND_TO_SERVER.listen(ClientPlayNetworking::send);
		PacketRegistry.registerTo((id, codec) -> {
			ClientPlayNetworking.registerGlobalReceiver(id, (packet, context) -> {
				packet.onClientReceive(context.client(), context.client().level, context.player());
			});
		});

		ClientEvents.registerModelLayers((id, layer) -> EntityModelLayerRegistry.registerModelLayer(id, layer::get));

		for (var layer : RenderLayers.LAYERS.entrySet())
			BlockRenderLayerMap.INSTANCE.putBlocks(layer.getKey(), layer.getValue().stream().map(Supplier::get).toArray(Block[]::new));

		ClientTickEvents.END_CLIENT_TICK.register(client -> WanderingWizardryClient.tickCounter++);

		ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new FabricPackReloader());

		ItemTooltipCallback.EVENT.register((stack, context, type, lines)
			-> ItemTooltipHandler.addTooltips(
				stack, type, lines::addAll
			)
		);
	}

	public static class FabricPackReloader extends PackReloader implements IdentifiableResourceReloadListener {
		@Override
		public ResourceLocation getFabricId() {
			return PackReloader.ID;
		}
	}
}
