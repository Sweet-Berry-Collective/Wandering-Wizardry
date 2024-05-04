package dev.sweetberry.wwizardry.neoforge.component;

import dev.sweetberry.wwizardry.api.component.Component;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class ProxyComponent<T extends Component> implements INBTSerializable {
	public T component;

	public ProxyComponent(T component) {
		this.component = component;
	}

	@Override
	public Tag serializeNBT(HolderLookup.Provider provider) {
		var tag = new CompoundTag();
		return (Tag) component.codec().encode(component, NbtOps.INSTANCE, tag).result().orElse(tag);
	}

	@Override
	public void deserializeNBT(HolderLookup.Provider provider, Tag nbt) {
		component = (T) component.codec().decode(NbtOps.INSTANCE, nbt).result().orElse(component);
	}
}
