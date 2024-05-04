package dev.sweetberry.wwizardry.content.potions;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.api.Lazy;
import dev.sweetberry.wwizardry.api.registry.RegistryContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.alchemy.Potion;

import java.util.function.Supplier;

public class PotionInitializer {
	public static final RegistryContext<Potion> POTIONS = new RegistryContext<>(BuiltInRegistries.POTION);

	public static final Lazy<Potion> WALL_GLUE = register("wall_glue", Potion::new);

	public static Lazy<Potion> register(String id, Supplier<Potion> potion) {
		return POTIONS.register(WanderingWizardry.id(id), potion);
	}
}
