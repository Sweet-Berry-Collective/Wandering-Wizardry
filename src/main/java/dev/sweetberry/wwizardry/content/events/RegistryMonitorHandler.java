package dev.sweetberry.wwizardry.content.events;

import dev.sweetberry.wwizardry.content.datagen.WallHolderBlockType;
import dev.sweetberry.wwizardry.content.datagen.DatagenInitializer;
import net.minecraft.block.Block;
import net.minecraft.block.CandleBlock;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import java.util.Arrays;

public class RegistryMonitorHandler {
	private static final String[] modid_exclusions = new String[] {
		"jello"
	};

	public static void onBlockAdded(Identifier id, Block block) {
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

	public static void registerHolderBlock(Block block, Identifier id, WallHolderBlockType.ParentType monostate, WallHolderBlockType.ParentType bistate) {
		registerHolderBlock(
			block,
			id,
			block.getDefaultState().contains(Properties.POWERED) ?
				bistate :
				monostate
		);
	}

	public static void registerHolderBlock(Block block, Identifier id, WallHolderBlockType.ParentType parentType) {
		DatagenInitializer.registerDataGenerator(WallHolderBlockType.transformId(id), new WallHolderBlockType(id, block, parentType));
	}
}
