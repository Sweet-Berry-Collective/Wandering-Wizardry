package dev.sweetberry.wwizardry.client.content.events;

import dev.sweetberry.wwizardry.Mod;
import dev.sweetberry.wwizardry.client.render.texture.AnimatedTexture;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSpriteManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Set;

// TODO: Do this without FAPI
public class PackReloader extends SimplePreparableReloadListener<Set<ResourceLocation>> implements IdentifiableResourceReloadListener {
	public static final ResourceLocation ID = Mod.id("animated_texture_loader");
	public static final FileToIdConverter LISTER = new FileToIdConverter("textures/wwizardry_animated", ".png");

	@Override
	public ResourceLocation getFabricId() {
		return ID;
	}

	@Override
	protected Set<ResourceLocation> prepare(ResourceManager resourceManager, ProfilerFiller profilerFiller) {
		return LISTER.listMatchingResources(resourceManager).keySet();
	}

	@Override
	protected void apply(Set<ResourceLocation> textures, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
		TextureManager textureManager = Minecraft.getInstance().getTextureManager();
		for (ResourceLocation texture : textures)
			textureManager.register(texture, new AnimatedTexture(texture));
	}
}
