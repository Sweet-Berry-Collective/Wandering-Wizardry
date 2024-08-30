package dev.sweetberry.wwizardry.mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(BlockBehaviour.class)
public interface Accessor_BlockBehaviour {
	@Invoker("getSoundType")
	SoundType invokeGetSoundType(BlockState state);

	@Invoker("getDrops")
	List<ItemStack> invokeGetDrops(BlockState state, LootParams.Builder builder);
}
