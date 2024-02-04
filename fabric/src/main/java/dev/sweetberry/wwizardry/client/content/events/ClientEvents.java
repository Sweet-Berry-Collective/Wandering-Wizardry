package dev.sweetberry.wwizardry.client.content.events;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.client.WanderingWizardryClient;
import dev.sweetberry.wwizardry.content.block.entity.AltarCatalyzerBlockEntity;
import dev.sweetberry.wwizardry.content.block.entity.AltarPedestalBlockEntity;
import dev.sweetberry.wwizardry.client.render.blockentity.AltarCatalyzerBlockEntityRenderer;
import dev.sweetberry.wwizardry.client.render.blockentity.AltarPedestalBlockEntityRenderer;
import dev.sweetberry.wwizardry.content.item.SoulMirrorItem;
import dev.sweetberry.wwizardry.content.item.VoidBagItem;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.server.packs.PackType;

public class ClientEvents {
	public static void init() {
		ItemTooltipCallback.EVENT.register(ItemTooltipHandler::addTooltips);

		BlockEntityRenderers.register(AltarPedestalBlockEntity.TYPE, AltarPedestalBlockEntityRenderer::new);
		BlockEntityRenderers.register(AltarCatalyzerBlockEntity.TYPE, AltarCatalyzerBlockEntityRenderer::new);

		ItemProperties.register(
			VoidBagItem.INSTANCE,
			WanderingWizardry.id("void_bag_closed"),
			ModelPredicates::getVoidBag
		);

		ItemProperties.register(
			SoulMirrorItem.INSTANCE,
			WanderingWizardry.id("cracked"),
			ModelPredicates::getSoulMirror
		);

		ClientTickEvents.END_CLIENT_TICK.register(client -> WanderingWizardryClient.tickCounter++);

		ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new PackReloader());
	}
}
