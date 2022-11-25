package io.github.sweetberrycollective.wwizardry.block;

import io.github.sweetberrycollective.wwizardry.WanderingMod;
import io.github.sweetberrycollective.wwizardry.block.entity.AltarCatalyzerBlockEntity;
import io.github.sweetberrycollective.wwizardry.block.entity.AltarPedestalBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

public class WanderingBlocks {
	public static void init() {
		registerBlock("altar_pedestal", AltarPedestalBlock.INSTANCE);
		registerBlockEntity("altar_pedestal", AltarPedestalBlockEntity.TYPE);
		registerBlock("altar_catalyzer", AltarCatalyzerBlock.INSTANCE);
		registerBlockEntity("altar_catalyzer", AltarCatalyzerBlockEntity.TYPE);
	}

	public static void registerBlock(String id, Block block) {
		Registry.register(Registry.BLOCK, WanderingMod.id(id), block);
	}
	public static <T extends BlockEntity> void registerBlockEntity(String id, BlockEntityType<T> blockEntity) {
		Registry.register(Registry.BLOCK_ENTITY_TYPE, WanderingMod.id(id), blockEntity);
	}
}
