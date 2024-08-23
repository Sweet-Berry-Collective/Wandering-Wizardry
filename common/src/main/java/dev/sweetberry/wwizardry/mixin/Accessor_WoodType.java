package dev.sweetberry.wwizardry.mixin;

import net.minecraft.world.level.block.state.properties.WoodType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(WoodType.class)
public interface Accessor_WoodType {
	@Accessor
	static Map<String, WoodType> getTYPES() {
		throw new IllegalCallerException();
	}
}
