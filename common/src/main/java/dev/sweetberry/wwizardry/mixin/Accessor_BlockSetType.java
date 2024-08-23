package dev.sweetberry.wwizardry.mixin;

import net.minecraft.world.level.block.state.properties.BlockSetType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(BlockSetType.class)
public interface Accessor_BlockSetType {
	@Accessor
	static Map<String, BlockSetType> getTYPES() {
		throw new IllegalCallerException();
	}
}
