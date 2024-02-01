package dev.sweetberry.wwizardry.content.painting;

import dev.sweetberry.wwizardry.Mod;
import dev.sweetberry.wwizardry.api.registry.RegistryContext;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.PaintingVariant;

public class PaintingInitializer {
	public static RegistryContext<PaintingVariant> PAINTINGS = new RegistryContext<>(BuiltInRegistries.PAINTING_VARIANT);

	public static final PaintingVariant ALTAR_PAINTING = registerPainting("altar", 32, 32);

	public static PaintingVariant registerPainting(String id, int width, int height) {
		return PAINTINGS.register(Mod.id(id), new PaintingVariant(width, height));
	}
}
