package dev.sweetberry.wwizardry.compat.sodium;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.client.render.texture.AnimatedTexture;
import net.caffeinemc.mods.sodium.client.render.texture.SpriteContentsExtension;

public class TextureMarker {
	private static void markTextureImpl(AnimatedTexture texture) {
		((SpriteContentsExtension)texture.contents).sodium$setActive(true);
	}

	public static void markTexture(AnimatedTexture texture) {
		if (WanderingWizardry.isModLoaded("sodium"))
			markTextureImpl(texture);
	}
}
