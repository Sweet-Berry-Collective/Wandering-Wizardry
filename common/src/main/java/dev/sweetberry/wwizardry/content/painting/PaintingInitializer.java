package dev.sweetberry.wwizardry.content.painting;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.api.Lazy;
import dev.sweetberry.wwizardry.api.registry.RegistryContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.decoration.PaintingVariant;

public class PaintingInitializer {
	public static RegistryContext<PaintingVariant> PAINTINGS = new RegistryContext<>(BuiltInRegistries.PAINTING_VARIANT);

	public static final Lazy<PaintingVariant> ALTAR_PAINTING = registerPainting("altar", 32, 32);

	public static Lazy<PaintingVariant> registerPainting(String id, int width, int height) {
		return PAINTINGS.register(WanderingWizardry.id(id), () -> new PaintingVariant(width, height));
	}
}
