package io.github.sweetberrycollective.wwizardry.client;

import io.github.sweetberrycollective.wwizardry.block.AltarCatalyzerBlock;
import io.github.sweetberrycollective.wwizardry.block.AltarPedestalBlock;
import io.github.sweetberrycollective.wwizardry.block.entity.AltarCatalyzerBlockEntity;
import io.github.sweetberrycollective.wwizardry.block.entity.AltarPedestalBlockEntity;
import io.github.sweetberrycollective.wwizardry.client.render.AltarCatalyzerBlockEntityRenderer;
import io.github.sweetberrycollective.wwizardry.client.render.AltarPedestalBlockEntityRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.block.extensions.api.client.BlockRenderLayerMap;
import org.quiltmc.qsl.lifecycle.api.client.event.ClientWorldTickEvents;

public class WanderingClient implements ClientModInitializer {
	public static int ITEM_ROTATION = 0;
	@Override
	public void onInitializeClient(ModContainer mod) {
		BlockEntityRendererFactories.register(AltarPedestalBlockEntity.TYPE, AltarPedestalBlockEntityRenderer::new);
		BlockEntityRendererFactories.register(AltarCatalyzerBlockEntity.TYPE, AltarCatalyzerBlockEntityRenderer::new);
		ClientWorldTickEvents.END.register((client, world) -> ITEM_ROTATION++);
		BlockRenderLayerMap.put(RenderLayer.getCutout(), AltarPedestalBlock.INSTANCE);
		BlockRenderLayerMap.put(RenderLayer.getCutout(), AltarCatalyzerBlock.INSTANCE);
	}
}
