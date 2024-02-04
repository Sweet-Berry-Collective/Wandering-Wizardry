package dev.sweetberry.wwizardry.client.render.texture;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.SpriteTicker;
import net.minecraft.client.renderer.texture.Tickable;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceMetadata;

import java.io.IOException;

public class AnimatedTexture extends AbstractTexture implements Tickable {
	public final ResourceLocation texture;

	private SpriteContents contents;
	private SpriteTicker ticker;

	public AnimatedTexture(ResourceLocation texture) {
		this.texture = texture;
	}

	@Override
	public void load(ResourceManager manager) throws IOException {
		var resource = manager.getResourceOrThrow(texture);
		var metadata = manager.getResource(texture.withSuffix(".mcmeta"));
		var nativeImage = NativeImage.read(resource.open());
		var resourceMetadata = metadata.isEmpty()
			? ResourceMetadata.EMPTY
			: ResourceMetadata.fromJsonStream(metadata.get().open());

		var animationMetadataSection = (AnimationMetadataSection) resourceMetadata
			.getSection(AnimationMetadataSection.SERIALIZER)
			.orElse(AnimationMetadataSection.EMPTY);
		var frameSize = animationMetadataSection.calculateFrameSize(nativeImage.getWidth(), nativeImage.getHeight());

		contents = new SpriteContents(
			texture,
			frameSize,
			nativeImage,
			metadata.isEmpty()
				? ResourceMetadata.EMPTY
				: ResourceMetadata.fromJsonStream(metadata.get().open())
		);
		if (RenderSystem.isOnRenderThreadOrInit())
			init(nativeImage);
		else
			RenderSystem.recordRenderCall(() -> init(nativeImage));
		this.ticker = contents.createTicker();
	}

	private void init(NativeImage image) {
		TextureUtil.prepareImage(this.getId(), 0, contents.width(), contents.height());
		contents.uploadFirstFrame(0, 0);
	}

	@Override
	public void tick() {
		if (ticker != null) {
			if (RenderSystem.isOnRenderThread()) {
				cycle();
			} else {
				RenderSystem.recordRenderCall(this::cycle);
			}
		}
	}

	private void cycle() {
//		SodiumIntegration.INSTANCE.markSpriteActive(contents);
		bind();
		ticker.tickAndUpload(0, 0);
	}
}
