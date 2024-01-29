package dev.sweetberry.wwizardry.client.content;

import dev.sweetberry.wwizardry.content.block.*;
import dev.sweetberry.wwizardry.content.block.altar.AltarCatalyzerBlock;
import dev.sweetberry.wwizardry.content.block.altar.AltarPedestalBlock;
import dev.sweetberry.wwizardry.content.block.nature.SculkflowerBlock;
import dev.sweetberry.wwizardry.content.block.redstone.ResonatorBlock;
import net.minecraft.client.render.RenderLayer;
import org.quiltmc.qsl.block.extensions.api.client.BlockRenderLayerMap;

public class RenderLayers {
	public static void init() {
		BlockRenderLayerMap.put(RenderLayer.getCutout(),
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
}
