package io.github.sweetberrycollective.wwizardry.client;

import io.github.sweetberrycollective.wwizardry.block.AltarCatalyzerBlock;
import io.github.sweetberrycollective.wwizardry.block.AltarPedestalBlock;
import io.github.sweetberrycollective.wwizardry.block.entity.AltarCatalyzerBlockEntity;
import io.github.sweetberrycollective.wwizardry.block.entity.AltarPedestalBlockEntity;
import io.github.sweetberrycollective.wwizardry.client.render.AltarCatalyzerBlockEntityRenderer;
import io.github.sweetberrycollective.wwizardry.client.render.AltarPedestalBlockEntityRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.block.extensions.api.client.BlockRenderLayerMap;
import org.quiltmc.qsl.lifecycle.api.client.event.ClientWorldTickEvents;

public class WanderingClient implements ClientModInitializer {
	public static float ITEM_ROTATION = 0;
	@Override
	public void onInitializeClient(ModContainer mod) {
		BlockEntityRendererRegistry.register(AltarPedestalBlockEntity.TYPE, AltarPedestalBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(AltarCatalyzerBlockEntity.TYPE, AltarCatalyzerBlockEntityRenderer::new);
		ClientWorldTickEvents.END.register((client, world) -> ITEM_ROTATION += 0.5f);
		BlockRenderLayerMap.put(RenderLayer.getCutout(), AltarPedestalBlock.INSTANCE);
		BlockRenderLayerMap.put(RenderLayer.getCutout(), AltarCatalyzerBlock.INSTANCE);
	}
}
