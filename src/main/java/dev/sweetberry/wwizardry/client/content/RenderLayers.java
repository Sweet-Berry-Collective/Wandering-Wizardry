package dev.sweetberry.wwizardry.client.content;

import dev.sweetberry.wwizardry.content.block.*;
import dev.sweetberry.wwizardry.content.block.altar.AltarCatalyzerBlock;
import dev.sweetberry.wwizardry.content.block.altar.AltarPedestalBlock;
import dev.sweetberry.wwizardry.content.block.nature.SculkflowerBlock;
import dev.sweetberry.wwizardry.content.block.redstone.ResonatorBlock;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

public class RenderLayers {
	public static void init() {
		put(RenderType.cutout(),
			AltarPedestalBlock.INSTANCE,
			AltarCatalyzerBlock.INSTANCE,
			CameraBlock.INSTANCE,
			ResonatorBlock.INSTANCE,
			SculkflowerBlock.INSTANCE,
			BlockInitializer.INDIGO_CAERULEUM,
			BlockInitializer.MODULO_COMPARATOR,
			BlockInitializer.REINFORCED_GLASS,
			BlockInitializer.REINFORCED_GLASS_PANE,
			BlockInitializer.MYCHA_ROOTS
		);
	}

	public static void put(RenderType layer, Block... blocks) {
		BlockRenderLayerMap.INSTANCE.putBlocks(layer, blocks);
	}
}
