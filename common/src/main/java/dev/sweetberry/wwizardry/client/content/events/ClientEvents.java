package dev.sweetberry.wwizardry.client.content.events;

import dev.sweetberry.wwizardry.api.Lazy;
import dev.sweetberry.wwizardry.content.block.BlockInitializer;
import dev.sweetberry.wwizardry.content.block.entity.AltarCatalyzerBlockEntity;
import dev.sweetberry.wwizardry.content.block.entity.AltarPedestalBlockEntity;
import dev.sweetberry.wwizardry.client.render.blockentity.AltarCatalyzerBlockEntityRenderer;
import dev.sweetberry.wwizardry.client.render.blockentity.AltarPedestalBlockEntityRenderer;
import dev.sweetberry.wwizardry.content.item.ItemInitializer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.function.BiConsumer;

public class ClientEvents {
	public static void registerModelPredicates(TriConsumer<Lazy<Item>, String, ClampedItemPropertyFunction> consumer) {
		consumer.accept(
			ItemInitializer.VOID_BAG,
			"void_bag_closed",
			ModelPredicates::getVoidBag
		);
		consumer.accept(
			(Lazy<Item>)(Object) ItemInitializer.SOUL_MIRROR,
			"cracked",
			ModelPredicates::getSoulMirror
		);
	}

	public static void registerEntityRenderers(BiConsumer<Lazy<BlockEntityType<?>>, BlockEntityRendererProvider<?>> consumer) {
		consumer.accept(
			(Lazy<BlockEntityType<?>>)(Object) BlockInitializer.ALTAR_PEDESTAL_TYPE,
			(BlockEntityRendererProvider<AltarPedestalBlockEntity>) AltarPedestalBlockEntityRenderer::new
		);
		consumer.accept(
			(Lazy<BlockEntityType<?>>)(Object) BlockInitializer.ALTAR_CATALYZER_TYPE,
			(BlockEntityRendererProvider<AltarCatalyzerBlockEntity>) AltarCatalyzerBlockEntityRenderer::new
		);
	}
}
