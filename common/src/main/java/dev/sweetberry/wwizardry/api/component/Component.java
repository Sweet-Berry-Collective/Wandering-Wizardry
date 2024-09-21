package dev.sweetberry.wwizardry.api.component;

import com.mojang.serialization.Codec;

public interface Component<TSelf extends Component<TSelf>> {
	Codec<TSelf> codec();

	void copyFrom(TSelf other);
}
