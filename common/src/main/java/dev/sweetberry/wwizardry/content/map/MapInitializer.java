package dev.sweetberry.wwizardry.content.map;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.api.Lazy;
import dev.sweetberry.wwizardry.api.registry.RegistryContext;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;

import java.util.function.Supplier;

public class MapInitializer {
	public static final RegistryContext<MapDecorationType> MAP_DECORATION_TYPE = new RegistryContext<>(BuiltInRegistries.MAP_DECORATION_TYPE);

	public static final Lazy<MapDecorationType> SCULK_LAB = registerMapDecorationType(
		"sculk_lab",
		() -> new MapDecorationType(
			WanderingWizardry.id("sculk_lab"),
			true,
			MapColor.COLOR_GRAY.col,
			true,
			true
		)
	);

	public static Lazy<MapDecorationType> registerMapDecorationType(String id, Supplier<MapDecorationType> item) {
		return MAP_DECORATION_TYPE.register(WanderingWizardry.id(id), item);
	}

	public static Holder<MapDecorationType> holder(Lazy<MapDecorationType> value) {
		return BuiltInRegistries.MAP_DECORATION_TYPE.getHolder(BuiltInRegistries.MAP_DECORATION_TYPE.getKey(value.get())).get();
	}
}
