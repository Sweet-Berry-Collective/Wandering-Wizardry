package dev.sweetberry.wwizardry.content.block;

import dev.sweetberry.wwizardry.Mod;
import dev.sweetberry.wwizardry.content.block.altar.AltarCatalyzerBlock;
import dev.sweetberry.wwizardry.content.block.altar.AltarPedestalBlock;
import dev.sweetberry.wwizardry.content.block.altar.entity.AltarCatalyzerBlockEntity;
import dev.sweetberry.wwizardry.content.block.altar.entity.AltarPedestalBlockEntity;
import dev.sweetberry.wwizardry.content.block.altar.entity.LogicGateBlockEntity;
import dev.sweetberry.wwizardry.content.block.nature.FallingDecayableBlock;
import dev.sweetberry.wwizardry.content.block.nature.RootedFlowerBlock;
import dev.sweetberry.wwizardry.content.block.nature.RootedPlantBlock;
import dev.sweetberry.wwizardry.content.block.nature.SculkflowerBlock;
import dev.sweetberry.wwizardry.content.block.redstone.LogicGateBlock;
import dev.sweetberry.wwizardry.content.block.redstone.ResonatorBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.block.SculkBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.ComparatorMode;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockInitializer {
	public static final BooleanProperty SCULK_INFESTED = BooleanProperty.create("sculked");
	public static final BooleanProperty SCULK_BELOW = BooleanProperty.create("sculk_below");

	public static final VoxelShape ALTAR_BASE_SHAPE = Shapes.or(
			Block.box(2.0, 0.0, 2.0, 14.0, 2.0, 14.0),
			Block.box(4.0, 2.0, 4.0, 12.0, 15.0, 12.0)
	).optimize();

	public static final Block INDIGO_CAERULEUM = registerBlock("indigo_caeruleum", new RootedFlowerBlock(MobEffects.INVISIBILITY, 20, "mycha_growable", FabricBlockSettings.copyOf(Blocks.POPPY)));

	public static final Block REINFORCED_GLASS = registerBlock("reinforced_glass", new TransparentBlock(FabricBlockSettings.copyOf(Blocks.GLASS).requiresCorrectToolForDrops()));

	public static final Block REINFORCED_GLASS_PANE = registerBlock("reinforced_glass_pane", new IronBarsBlock(FabricBlockSettings.copyOf(Blocks.GLASS).requiresCorrectToolForDrops()));

	public static final Block REDSTONE_LANTERN = registerBlock("redstone_lantern", new RedstoneLampBlock(FabricBlockSettings.copyOf(Blocks.REDSTONE_LAMP)));

	public static final Block ROSE_QUARTZ_ORE = registerBlock("rose_quartz_ore", new DropExperienceBlock(UniformInt.of(1,4), FabricBlockSettings.copyOf(Blocks.IRON_ORE)));
	public static final Block DEEPSLATE_ROSE_QUARTZ_ORE = registerBlock("deepslate_rose_quartz_ore", new DropExperienceBlock(UniformInt.of(1,4), FabricBlockSettings.copyOf(Blocks.DEEPSLATE_IRON_ORE)));
	public static final Block ROSE_QUARTZ_BLOCK = registerBlock("rose_quartz_block", new Block(FabricBlockSettings.copyOf(Blocks.AMETHYST_BLOCK)));

	public static final Block MODULO_COMPARATOR = registerBlock(
		"modulo_comparator",
		new LogicGateBlock(
			FabricBlockSettings.copyOf(Blocks.COMPARATOR),
			LogicGateBlock.SideInput.ALL,
			true,
			(state, mode, side, back) -> {
				int value = side == 0 ? 0 : back % side;
				if (mode == ComparatorMode.SUBTRACT)
					value = back - value;
				return value;
			}
		)
	);

	public static final Block REDSTONE_STEPPER = registerBlock(
		"redstone_stepper",
		new LogicGateBlock(
			FabricBlockSettings.copyOf(Blocks.REPEATER),
			LogicGateBlock.SideInput.NONE,
			false,
			(state, mode, side, back) -> back > 0 ? 1 : 0
		)
	);

	public static final Block MYCELIAL_SAND = registerBlock(
		"mycelial_sand",
		new FallingDecayableBlock(
			FabricBlockSettings.copyOf(Blocks.SAND).mapColor(MapColor.ICE),
			Blocks.SAND,
			"mycha_spread"
		)
	);

	public static final Block MYCHA_ROOTS = registerBlock(
		"mycha_roots",
		new RootedPlantBlock(
			BlockBehaviour.Properties.of()
				.mapColor(MapColor.NETHER)
				.replaceable()
				.noCollission()
				.instabreak()
				.sound(SoundType.ROOTS)
				.offsetType(BlockBehaviour.OffsetType.XZ)
				.pushReaction(PushReaction.DESTROY),
			"mycha"
		)
	);

	public static void init() {
		registerBlock("altar_pedestal", AltarPedestalBlock.INSTANCE);
		registerBlock("altar_catalyzer", AltarCatalyzerBlock.INSTANCE);
		registerBlock("sculk_resonator", ResonatorBlock.INSTANCE);
		registerBlock("sculkflower", SculkflowerBlock.INSTANCE);
		registerBlock("crystalline_sculk_block", CrystalSculkBlock.INSTANCE);
		registerBlock("camera", CameraBlock.INSTANCE);
		registerBlock("wall_holder", WallHolderBlock.EMPTY);

		registerBlockEntity("altar_pedestal", AltarPedestalBlockEntity.TYPE);
		registerBlockEntity("altar_catalyzer", AltarCatalyzerBlockEntity.TYPE);
		registerBlockEntity("extensible_comparator", LogicGateBlockEntity.TYPE);
	}

	public static <T extends Block> T registerBlock(String id, T block) {
		return Registry.register(BuiltInRegistries.BLOCK, Mod.id(id), block);
	}

	public static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(String id, BlockEntityType<T> blockEntity) {
		return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Mod.id(id), blockEntity);
	}

	public static boolean testForSculk(BlockGetter world, BlockPos pos) {
		var state = world.getBlockState(pos);
		return state.getBlock() instanceof SculkBlock || state.getBlock() == Blocks.AIR || !state.isFaceSturdy(world, pos, Direction.UP);
	}
}
