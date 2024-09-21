package dev.sweetberry.wwizardry.client.content.events;

import dev.sweetberry.wwizardry.api.Lazy;
import dev.sweetberry.wwizardry.client.WanderingWizardryClient;
import dev.sweetberry.wwizardry.client.render.model.SnailModel;
import dev.sweetberry.wwizardry.client.render.entity.SnailRenderer;
import dev.sweetberry.wwizardry.client.render.model.AltarCatalyzerModel;
import dev.sweetberry.wwizardry.content.block.BlockInitializer;
import dev.sweetberry.wwizardry.client.render.blockentity.AltarCatalyzerBlockEntityRenderer;
import dev.sweetberry.wwizardry.client.render.blockentity.AltarPedestalBlockEntityRenderer;
import dev.sweetberry.wwizardry.content.component.BoatComponent;
import dev.sweetberry.wwizardry.content.entity.EntityInitializer;
import dev.sweetberry.wwizardry.content.item.ItemInitializer;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
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

	private static <T extends Entity> EntityRendererProvider<Entity> get(EntityRendererProvider<T> provider) {
		return (EntityRendererProvider<Entity>) provider;
	}

	private static <T extends BlockEntity> BlockEntityRendererProvider<BlockEntity> get(BlockEntityRendererProvider<T> provider) {
		return (BlockEntityRendererProvider<BlockEntity>) provider;
	}

	public static void registerEntityRenderers(BiConsumer<Lazy<EntityType<Entity>>, EntityRendererProvider<Entity>> consumer) {
		consumer.accept(
			(Lazy<EntityType<Entity>>) (Object) EntityInitializer.SNAIL,
			get(SnailRenderer::new)
		);
	}

	public static void registerBlockEntityRenderers(BiConsumer<Lazy<BlockEntityType<BlockEntity>>, BlockEntityRendererProvider<BlockEntity>> consumer) {
		consumer.accept(
			(Lazy<BlockEntityType<BlockEntity>>)(Object) BlockInitializer.ALTAR_PEDESTAL_TYPE,
			get(AltarPedestalBlockEntityRenderer::new)
		);
		consumer.accept(
			(Lazy<BlockEntityType<BlockEntity>>)(Object) BlockInitializer.ALTAR_CATALYZER_TYPE,
			get(AltarCatalyzerBlockEntityRenderer::new)
		);
	}

	public static void registerModelLayers(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> consumer) {
		var boatModel = BoatModel.createBodyModel();
		var chestBoatModel = ChestBoatModel.createBodyModel();

		for (var id : BoatComponent.BOATS.keySet()) {
			consumer.accept(WanderingWizardryClient.getBoatLayerLocation(id, false), () -> boatModel);
			consumer.accept(WanderingWizardryClient.getBoatLayerLocation(id, true), () -> chestBoatModel);
		}

		var altarModel = AltarCatalyzerModel.createLayer();
		consumer.accept(AltarCatalyzerModel.LAYER_LOCATION, () -> altarModel);

		var snailModel = SnailModel.createBodyLayer();
		consumer.accept(SnailModel.LAYER_LOCATION, () -> snailModel);
	}
}
