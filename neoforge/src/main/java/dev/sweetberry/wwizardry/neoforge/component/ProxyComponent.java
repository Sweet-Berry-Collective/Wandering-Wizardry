package dev.sweetberry.wwizardry.neoforge.component;

import dev.sweetberry.wwizardry.api.component.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class ProxyComponent<T extends Component> implements INBTSerializable {
	public final T component;

	public ProxyComponent(T component) {
		this.component = component;
	}

	@Override
	public Tag serializeNBT() {
		var tag = new CompoundTag();
		component.toNbt(tag);
		return tag;
	}

	@Override
	public void deserializeNBT(Tag nbt) {
		if (nbt instanceof CompoundTag tag)
			component.fromNbt(tag);
	}
}
