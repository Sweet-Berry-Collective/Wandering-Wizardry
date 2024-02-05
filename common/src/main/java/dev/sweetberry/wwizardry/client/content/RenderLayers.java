package dev.sweetberry.wwizardry.client.content;

import dev.sweetberry.wwizardry.content.block.*;
import dev.sweetberry.wwizardry.content.block.altar.AltarCatalyzerBlock;
import dev.sweetberry.wwizardry.content.block.altar.AltarPedestalBlock;
import dev.sweetberry.wwizardry.content.block.nature.SculkflowerBlock;
import dev.sweetberry.wwizardry.content.block.redstone.ResonatorBlock;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RenderLayers {
	public static Map<RenderType, Set<Block>> LAYERS = new HashMap<>();

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
		var set = getLayer(layer);
		for (var block : blocks)
			set.add(block);
	}

	private static Set<Block> getLayer(RenderType layer) {
		if (!LAYERS.containsKey(layer))
			LAYERS.put(layer, new HashSet<>());
		return LAYERS.get(layer);
	}
}
