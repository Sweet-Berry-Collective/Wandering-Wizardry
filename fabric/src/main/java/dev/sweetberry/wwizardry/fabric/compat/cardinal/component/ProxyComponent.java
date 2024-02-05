package dev.sweetberry.wwizardry.fabric.compat.cardinal.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.sweetberry.wwizardry.api.component.Component;
import net.minecraft.nbt.CompoundTag;

public class ProxyComponent<T extends Component> implements AutoSyncedComponent {
	public T baseComponent;

	public ProxyComponent(T base) {
		baseComponent = base;
	}

	@Override
    public void readFromNbt(CompoundTag tag) {
		baseComponent.fromNbt(tag);
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		baseComponent.toNbt(tag);
	}
}
