package dev.sweetberry.wwizardry.fabric.compat.cardinal.component;

import dev.sweetberry.wwizardry.api.component.Component;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class ProxyComponent<T extends Component> implements AutoSyncedComponent {
	public T baseComponent;

	public ProxyComponent(T base) {
		baseComponent = base;
	}

	@Override
    public void readFromNbt(CompoundTag tag, HolderLookup.Provider lookup) {
		baseComponent.fromNbt(tag, lookup);
	}

	@Override
	public void writeToNbt(CompoundTag tag, HolderLookup.Provider lookup) {
		baseComponent.toNbt(tag, lookup);
	}
}
