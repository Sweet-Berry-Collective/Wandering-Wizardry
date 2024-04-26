package dev.sweetberry.wwizardry.api.component;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

public interface Component {
	void fromNbt(CompoundTag tag, HolderLookup.Provider lookup);
	void toNbt(CompoundTag tag, HolderLookup.Provider lookup);
}
