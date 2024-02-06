package dev.sweetberry.wwizardry.fabric.compat.cardinal;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.sweetberry.wwizardry.fabric.compat.cardinal.component.BoatCardinalComponent;
import dev.sweetberry.wwizardry.fabric.compat.cardinal.component.ProxyComponent;
import dev.sweetberry.wwizardry.fabric.compat.cardinal.component.VoidBagCardinalComponent;
import dev.sweetberry.wwizardry.content.component.ComponentInitializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;

import java.util.HashMap;
import java.util.Map;

public class CardinalInitializer implements EntityComponentInitializer {
	public static final Map<ResourceLocation, ComponentKey<ProxyComponent<?>>> COMPONENTS = new HashMap<>();
	public static final ComponentKey<VoidBagCardinalComponent> VOID_BAG = createComponent(
		ComponentInitializer.VOID_BAG,
		VoidBagCardinalComponent.class
	);

	public static final ComponentKey<BoatCardinalComponent> BOAT = createComponent(
		ComponentInitializer.BOAT,
		BoatCardinalComponent.class
	);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerForPlayers(VOID_BAG, VoidBagCardinalComponent::new);
		registry.registerFor(Boat.class, BOAT, BoatCardinalComponent::new);
	}

	public static <T extends ProxyComponent<?>> ComponentKey<T> createComponent(ResourceLocation id, Class<T> clazz) {
		var key = ComponentRegistryV3.INSTANCE
			.getOrCreate(
				id,
				clazz
			);
		COMPONENTS.put(id, (ComponentKey<ProxyComponent<?>>) key);
		return key;
	}
}
