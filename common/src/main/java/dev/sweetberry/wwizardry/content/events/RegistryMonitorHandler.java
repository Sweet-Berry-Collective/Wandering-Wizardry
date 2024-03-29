package dev.sweetberry.wwizardry.content.events;

import dev.sweetberry.wwizardry.content.datagen.WallHolderBlockType;
import dev.sweetberry.wwizardry.content.datagen.DatagenInitializer;
import java.util.Arrays;
import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class RegistryMonitorHandler {
	private static final String[] modid_exclusions = new String[] {
		"jello"
	};

	public static void onBlockAdded(Registry<Block> registry, ResourceLocation id, Supplier<Block> supplier) {
		var block = supplier.get();
		// Ignore known broken mods. TODO: Make this configurable
		if (
			Arrays.stream(modid_exclusions)
				.anyMatch(
					it -> it.equals(id.getNamespace())
				)
		)
			return;
		if (block instanceof CandleBlock)
			registerHolderBlock(block, id, WallHolderBlockType.ParentType.CANDLE);
		// Blocked out for now, until I can implement them
//        if (block instanceof TorchBlock)
//            registerHolderBlock(block, id, ParentType.TORCH, ParentType.TORCH_TOGGLEABLE);
//        if (block instanceof LanternBlock)
//            registerHolderBlock(block, id, ParentType.LANTERN, ParentType.LANTERN_TOGGLEABLE);
	}

	public static void registerHolderBlock(Block block, ResourceLocation id, WallHolderBlockType.ParentType monostate, WallHolderBlockType.ParentType bistate) {
		registerHolderBlock(
			block,
			id,
			block.defaultBlockState().hasProperty(BlockStateProperties.POWERED) ?
				bistate :
				monostate
		);
	}

	public static void registerHolderBlock(Block block, ResourceLocation id, WallHolderBlockType.ParentType parentType) {
		DatagenInitializer.registerDataGenerator(WallHolderBlockType.transformId(id), new WallHolderBlockType(id, block, parentType));
	}
}
