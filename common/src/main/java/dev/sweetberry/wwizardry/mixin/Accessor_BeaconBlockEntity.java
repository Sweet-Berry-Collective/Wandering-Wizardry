package dev.sweetberry.wwizardry.mixin;

import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BeaconBlockEntity.class)
public interface Accessor_BeaconBlockEntity {
	@Accessor
	int getLastCheckY();

	@Accessor
	void setLastCheckY(int value);
}
