package io.github.sweetberrycollective.wwizardry.block;

import io.github.sweetberrycollective.wwizardry.WanderingMod;
import io.github.sweetberrycollective.wwizardry.block.entity.AltarCatalyzerBlockEntity;
import io.github.sweetberrycollective.wwizardry.block.entity.AltarPedestalBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

public class WanderingBlocks {
	public static final BooleanProperty SCULK_INFESTED = BooleanProperty.of("sculked");
	public static final BooleanProperty SCULK_BELOW = BooleanProperty.of("sculk_below");

	public static final VoxelShape ALTAR_BASE_SHAPE = VoxelShapes.union(
			Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 2.0, 14.0),
			Block.createCuboidShape(4.0, 2.0, 4.0, 12.0, 15.0, 12.0)
	).simplify();

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
