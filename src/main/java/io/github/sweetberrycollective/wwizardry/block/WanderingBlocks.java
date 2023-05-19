package io.github.sweetberrycollective.wwizardry.block;

import io.github.sweetberrycollective.wwizardry.WanderingMod;
import io.github.sweetberrycollective.wwizardry.block.entity.AltarCatalyzerBlockEntity;
import io.github.sweetberrycollective.wwizardry.block.entity.AltarPedestalBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;

public class WanderingBlocks {
	public static final BooleanProperty SCULK_INFESTED = BooleanProperty.of("sculked");
	public static final BooleanProperty SCULK_BELOW = BooleanProperty.of("sculk_below");

	public static final VoxelShape ALTAR_BASE_SHAPE = VoxelShapes.union(
			Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 2.0, 14.0),
			Block.createCuboidShape(4.0, 2.0, 4.0, 12.0, 15.0, 12.0)
	).simplify();

	public static final Block INDIGO_CAERULEUM = registerBlock("indigo_caeruleum", new WanderingFlowerBlock(StatusEffects.INVISIBILITY, 20, QuiltBlockSettings.copyOf(Blocks.POPPY)));

	public static final Block REINFORCED_GLASS = registerBlock("reinforced_glass", new GlassBlock(QuiltBlockSettings.copyOf(Blocks.GLASS)));

	public static final Block REINFORCED_GLASS_PANE = registerBlock("reinforced_glass_pane", new PaneBlock(QuiltBlockSettings.copyOf(Blocks.GLASS)));

	public static final Block REDSTONE_LANTERN = registerBlock("redstone_lantern", new RedstoneLampBlock(QuiltBlockSettings.copyOf(Blocks.REDSTONE_LAMP)));

	public static void init() {
		registerBlock("altar_pedestal", AltarPedestalBlock.INSTANCE);
		registerBlockEntity("altar_pedestal", AltarPedestalBlockEntity.TYPE);
		registerBlock("altar_catalyzer", AltarCatalyzerBlock.INSTANCE);
		registerBlockEntity("altar_catalyzer", AltarCatalyzerBlockEntity.TYPE);
		registerBlock("sculkflower", SculkflowerBlock.INSTANCE);
		registerBlock("crystalline_sculk_block", CrystalSculkBlock.INSTANCE);
		registerBlock("camera", CameraBlock.INSTANCE);
		registerBlock("wall_holder", WallHolderBlock.EMPTY);
	}

	public static <T extends Block> T registerBlock(String id, T block) {
		return Registry.register(Registries.BLOCK, WanderingMod.id(id), block);
	}

	public static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(String id, BlockEntityType<T> blockEntity) {
		return Registry.register(Registries.BLOCK_ENTITY_TYPE, WanderingMod.id(id), blockEntity);
	}

	public static boolean testForSculk(BlockView world, BlockPos pos) {
		var state = world.getBlockState(pos);
		return state.getBlock() instanceof SculkBlock || state.getBlock() == Blocks.AIR || !state.isSideSolidFullSquare(world, pos, Direction.UP);
	}
}
