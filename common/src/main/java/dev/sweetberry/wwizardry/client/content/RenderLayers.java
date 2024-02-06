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
import java.util.function.Supplier;

public class RenderLayers {
	public static Map<RenderType, Set<Supplier<Block>>> LAYERS = new HashMap<>();

	public static void init() {
		put(RenderType.cutout(),
			BlockInitializer.ALTAR_PEDESTAL,
			(Supplier<Block>)(Object) BlockInitializer.ALTAR_CATALYZER,
			BlockInitializer.CAMERA,
			BlockInitializer.SCULK_RESONATOR,
			BlockInitializer.SCULKFLOWER,
			BlockInitializer.INDIGO_CAERULEUM,
			BlockInitializer.MODULO_COMPARATOR,
			BlockInitializer.REINFORCED_GLASS,
			BlockInitializer.REINFORCED_GLASS_PANE,
			BlockInitializer.MYCHA_ROOTS
		);
	}

	public static void put(RenderType layer, Supplier<Block>... blocks) {
		var set = getLayer(layer);
		for (var block : blocks)
			set.add(block);
	}

	private static Set<Supplier<Block>> getLayer(RenderType layer) {
		if (!LAYERS.containsKey(layer))
			LAYERS.put(layer, new HashSet<>());
		return LAYERS.get(layer);
	}
}
