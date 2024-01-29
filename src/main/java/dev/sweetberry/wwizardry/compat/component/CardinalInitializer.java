package dev.sweetberry.wwizardry.compat.component;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.sweetberry.wwizardry.Mod;

public class CardinalInitializer implements EntityComponentInitializer {
	public static final ComponentKey<VoidBagComponent> VOID_BAG = ComponentRegistryV3.INSTANCE
		.getOrCreate(
			Mod.id("void_bag"),
			VoidBagComponent.class
		);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerForPlayers(VOID_BAG, VoidBagComponent::new);
	}
}
