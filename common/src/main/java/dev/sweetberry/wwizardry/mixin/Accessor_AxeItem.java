package dev.sweetberry.wwizardry.mixin;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(AxeItem.class)
public interface Accessor_AxeItem {
	@Accessor("STRIPPABLES")
	static Map<Block, Block> getStrippedBlocks() {
		throw new AssertionError("Untransformed @Accessor");
	}

	@Accessor("STRIPPABLES")
	@Mutable
	static void setStrippedBlocks(Map<Block, Block> strippedBlocks) {
		throw new AssertionError("Untransformed @Accessor");
	}
}
