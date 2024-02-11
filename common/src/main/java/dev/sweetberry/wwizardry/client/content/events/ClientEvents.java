package dev.sweetberry.wwizardry.client.content.events;

import dev.sweetberry.wwizardry.api.Lazy;
import dev.sweetberry.wwizardry.client.WanderingWizardryClient;
import dev.sweetberry.wwizardry.client.render.model.AltarCatalyzerModel;
import dev.sweetberry.wwizardry.content.block.BlockInitializer;
import dev.sweetberry.wwizardry.content.block.entity.AltarCatalyzerBlockEntity;
import dev.sweetberry.wwizardry.content.block.entity.AltarPedestalBlockEntity;
import dev.sweetberry.wwizardry.client.render.blockentity.AltarCatalyzerBlockEntityRenderer;
import dev.sweetberry.wwizardry.client.render.blockentity.AltarPedestalBlockEntityRenderer;
import dev.sweetberry.wwizardry.content.block.sign.ModdedSignBlock;
import dev.sweetberry.wwizardry.content.component.BoatComponent;
import dev.sweetberry.wwizardry.content.item.ItemInitializer;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ClientEvents {
	public static void registerModelPredicates(TriConsumer<Lazy<Item>, String, ClampedItemPropertyFunction> consumer) {
		consumer.accept(
			ItemInitializer.VOID_BAG,
			"void_bag_closed",
			ModelPredicates::getVoidBag
		);
		consumer.accept(
			(Lazy<Item>)(Object) ItemInitializer.SOUL_MIRROR,
			"cracked",
			ModelPredicates::getSoulMirror
		);
	}

	public static void registerEntityRenderers(BiConsumer<Lazy<BlockEntityType<?>>, BlockEntityRendererProvider<?>> consumer) {
		consumer.accept(
			(Lazy<BlockEntityType<?>>)(Object) BlockInitializer.ALTAR_PEDESTAL_TYPE,
			(BlockEntityRendererProvider<AltarPedestalBlockEntity>) AltarPedestalBlockEntityRenderer::new
		);
		consumer.accept(
			(Lazy<BlockEntityType<?>>)(Object) BlockInitializer.ALTAR_CATALYZER_TYPE,
			(BlockEntityRendererProvider<AltarCatalyzerBlockEntity>) AltarCatalyzerBlockEntityRenderer::new
		);
	}

	public static void registerModelLayers(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> consumer) {
		var boatModel = BoatModel.createBodyModel();
		var chestBoatModel = ChestBoatModel.createBodyModel();

		for (var id : BoatComponent.BOATS.keySet()) {
			consumer.accept(WanderingWizardryClient.getBoatLayerLocation(id, false), () -> boatModel);
			consumer.accept(WanderingWizardryClient.getBoatLayerLocation(id, true), () -> chestBoatModel);
		}

		var signModel = SignRenderer.createSignLayer();
		var hangingSignModel = HangingSignRenderer.createHangingSignLayer();

		for (var id : ModdedSignBlock.SIGNS) {
			consumer.accept(WanderingWizardryClient.getSignLayerLocation(id, false), () -> signModel);
			consumer.accept(WanderingWizardryClient.getSignLayerLocation(id, true), () -> hangingSignModel);
		}

		var altarModel = AltarCatalyzerModel.createLayer();
		consumer.accept(AltarCatalyzerModel.LAYER_LOCATION, () -> altarModel);
	}
}
