package dev.sweetberry.wwizardry.api.component;

import net.minecraft.nbt.CompoundTag;

public interface Component {
	void fromNbt(CompoundTag tag);
	void toNbt(CompoundTag tag);
}
