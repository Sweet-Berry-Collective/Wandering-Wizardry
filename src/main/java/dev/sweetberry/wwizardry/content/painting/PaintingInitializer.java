package dev.sweetberry.wwizardry.content.painting;

import dev.sweetberry.wwizardry.Mod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.PaintingVariant;

public class PaintingInitializer {
	public static final ResourceKey<PaintingVariant> ALTAR_PAINTING = registerPainting(
		Mod.id("altar"),
		32, 32
	);

	public static void init() {}

	public static ResourceKey<PaintingVariant> registerPainting(ResourceLocation id, int width, int height) {
		final var key = ResourceKey.create(
			Registries.PAINTING_VARIANT,
			id
		);
		Registry.register(BuiltInRegistries.PAINTING_VARIANT, key, new PaintingVariant(width, height));
		return key;
	}
}
