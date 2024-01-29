package dev.sweetberry.wwizardry.client.content.events;

import dev.sweetberry.wwizardry.Mod;
import dev.sweetberry.wwizardry.client.ModClient;
import dev.sweetberry.wwizardry.content.block.altar.entity.AltarCatalyzerBlockEntity;
import dev.sweetberry.wwizardry.content.block.altar.entity.AltarPedestalBlockEntity;
import dev.sweetberry.wwizardry.client.render.AltarCatalyzerBlockEntityRenderer;
import dev.sweetberry.wwizardry.client.render.AltarPedestalBlockEntityRenderer;
import dev.sweetberry.wwizardry.content.item.SoulMirrorItem;
import dev.sweetberry.wwizardry.content.item.VoidBagItem;
import net.fabricmc.fabric.api.event.client.ItemTooltipCallback;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import org.quiltmc.qsl.lifecycle.api.client.event.ClientWorldTickEvents;

public class ClientEvents {
	public static void init() {
		ItemTooltipCallback.EVENT.register(ItemTooltipHandler::addTooltips);

		BlockEntityRendererFactories.register(AltarPedestalBlockEntity.TYPE, AltarPedestalBlockEntityRenderer::new);
		BlockEntityRendererFactories.register(AltarCatalyzerBlockEntity.TYPE, AltarCatalyzerBlockEntityRenderer::new);

		ModelPredicateProviderRegistry.register(
			VoidBagItem.INSTANCE,
			Mod.id("void_bag_closed"),
			ModelPredicates::getVoidBag
		);

		ModelPredicateProviderRegistry.register(
			SoulMirrorItem.INSTANCE,
			Mod.id("cracked"),
			ModelPredicates::getSoulMirror
		);

		ClientWorldTickEvents.END.register((client, world) -> ModClient.tickCounter++);
	}
}
