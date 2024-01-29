package dev.sweetberry.wwizardry.content.painting;

import dev.sweetberry.wwizardry.Mod;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class PaintingInitializer {
	public static final RegistryKey<PaintingVariant> ALTAR_PAINTING = registerPainting(
		Mod.id("altar"),
		32, 32
	);

	public static void init() {}

	public static RegistryKey<PaintingVariant> registerPainting(Identifier id, int width, int height) {
		final var key = RegistryKey.of(
			RegistryKeys.PAINTING_VARIANT,
			id
		);
		Registry.register(Registries.PAINTING_VARIANT, key, new PaintingVariant(width, height));
		return key;
	}
}
