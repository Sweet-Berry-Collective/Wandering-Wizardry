package dev.sweetberry.wwizardry.client;

import com.terraformersmc.terraform.boat.api.client.TerraformBoatClientHelper;
import com.terraformersmc.terraform.sign.SpriteIdentifierRegistry;
import dev.sweetberry.wwizardry.block.*;
import dev.sweetberry.wwizardry.client.render.AltarPedestalBlockEntityRenderer;
import dev.sweetberry.wwizardry.WanderingMod;
import dev.sweetberry.wwizardry.block.entity.AltarCatalyzerBlockEntity;
import dev.sweetberry.wwizardry.block.entity.AltarPedestalBlockEntity;
import dev.sweetberry.wwizardry.client.render.AltarCatalyzerBlockEntityRenderer;
import dev.sweetberry.wwizardry.datagen.WanderingDatagen;
import dev.sweetberry.wwizardry.datagen.WoodType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.util.SpriteIdentifier;
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
		BlockRenderLayerMap.put(RenderLayer.getCutout(), SculkflowerBlock.INSTANCE);
		BlockRenderLayerMap.put(RenderLayer.getCutout(), CameraBlock.INSTANCE);
		BlockRenderLayerMap.put(RenderLayer.getCutout(), WanderingBlocks.INDIGO_CAERULEUM);
		BlockRenderLayerMap.put(RenderLayer.getCutout(), WanderingBlocks.REINFORCED_GLASS);
		BlockRenderLayerMap.put(RenderLayer.getCutout(), WanderingBlocks.REINFORCED_GLASS_PANE);
		WanderingDatagen.REGISTRY.forEach(dataGenerator -> {
			if (dataGenerator instanceof WoodType woodType) {
				BlockRenderLayerMap.put(RenderLayer.getCutout(), woodType.DOOR, woodType.TRAPDOOR);
				SpriteIdentifierRegistry.INSTANCE.addIdentifier(new SpriteIdentifier(TexturedRenderLayers.SIGNS_ATLAS_TEXTURE, woodType.SIGN.getTexture()));
				SpriteIdentifierRegistry.INSTANCE.addIdentifier(new SpriteIdentifier(TexturedRenderLayers.SIGNS_ATLAS_TEXTURE, woodType.HANGING_SIGN.getTexture()));
				if (!woodType.fungus) {
					BlockRenderLayerMap.put(RenderLayer.getCutout(), woodType.LEAVES);
					TerraformBoatClientHelper.registerModelLayers(WanderingMod.id(woodType.baseName), false);
				}
			}
		});
	}
}
