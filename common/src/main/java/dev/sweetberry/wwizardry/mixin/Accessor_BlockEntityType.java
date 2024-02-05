package dev.sweetberry.wwizardry.mixin;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(BlockEntityType.class)
public interface Accessor_BlockEntityType {
	@Accessor
	Set<Block> getValidBlocks();

	@Accessor
	@Mutable
	void setValidBlocks(Set<Block> blocks);
}
