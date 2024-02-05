package dev.sweetberry.wwizardry.client.content.events;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.client.content.AnimatedTextureMap;
import dev.sweetberry.wwizardry.client.render.texture.AnimatedTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Set;

public class PackReloader extends SimplePreparableReloadListener<Set<ResourceLocation>> {
	public static final ResourceLocation ID = WanderingWizardry.id("animated_texture_loader");
	public static final FileToIdConverter LISTER = new FileToIdConverter("textures/wwizardry_animated", ".png");

	@Override
	protected Set<ResourceLocation> prepare(ResourceManager resourceManager, ProfilerFiller profilerFiller) {
		return LISTER.listMatchingResources(resourceManager).keySet();
	}

	@Override
	protected void apply(Set<ResourceLocation> textures, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
		AnimatedTextureMap.ANIMATED_TEXTURES.clear();
		var textureManager = Minecraft.getInstance().getTextureManager();
		for (ResourceLocation texture : textures) {
			textureManager.register(texture, new AnimatedTexture(texture));
			AnimatedTextureMap.ANIMATED_TEXTURES.add(texture);
		}
	}
}
