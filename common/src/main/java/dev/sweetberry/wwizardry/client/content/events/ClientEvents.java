package dev.sweetberry.wwizardry.client.content.events;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.content.block.entity.AltarCatalyzerBlockEntity;
import dev.sweetberry.wwizardry.content.block.entity.AltarPedestalBlockEntity;
import dev.sweetberry.wwizardry.client.render.blockentity.AltarCatalyzerBlockEntityRenderer;
import dev.sweetberry.wwizardry.client.render.blockentity.AltarPedestalBlockEntityRenderer;
import dev.sweetberry.wwizardry.content.item.SoulMirrorItem;
import dev.sweetberry.wwizardry.content.item.VoidBagItem;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;

public class ClientEvents {
	public static void init() {
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
	}
}
