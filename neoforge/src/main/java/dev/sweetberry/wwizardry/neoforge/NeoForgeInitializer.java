package dev.sweetberry.wwizardry.neoforge;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.api.Lazy;
import dev.sweetberry.wwizardry.client.WanderingWizardryClient;
import dev.sweetberry.wwizardry.client.content.events.ClientEvents;
import dev.sweetberry.wwizardry.client.content.events.PackReloader;
import dev.sweetberry.wwizardry.compat.terrablender.TerraBlenderInitializer;
import dev.sweetberry.wwizardry.content.ContentInitializer;
import dev.sweetberry.wwizardry.content.block.sign.ModdedSignBlock;
import dev.sweetberry.wwizardry.content.component.BoatComponent;
import dev.sweetberry.wwizardry.content.item.ItemInitializer;
import dev.sweetberry.wwizardry.neoforge.component.NeoForgeComponents;
import dev.sweetberry.wwizardry.neoforge.networking.NeoForgeNetworking;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.stream.Collectors;

@Mod("wwizardry")
public class NeoForgeInitializer {
	public static final Lazy<CreativeModeTab> TAB = ItemInitializer.registerTab(
		"items",
		() -> CreativeModeTab.builder()
			.icon(() -> ItemInitializer.CRYSTALLINE_SCULK_SHARD.get().getDefaultInstance())
			.displayItems((display, collector) -> collector.acceptAll(ItemInitializer.STACKS.stream().map(Lazy::get).map(Item::getDefaultInstance).collect(Collectors.toList())))
			.title(net.minecraft.network.chat.Component.translatable("itemGroup.wwizardry.items"))
			.build()
	);

	public NeoForgeInitializer(IEventBus bus, Dist dist) {
		bus.addListener(this::registerToRegistries);
		bus.addListener(this::commonSetup);
		WanderingWizardry.modLoadedCheck = ModList.get()::isLoaded;
		NeoForgeEvents.init();
		NeoForgeComponents.init(bus);
		NeoForgeNetworking.init(bus);
		WanderingWizardry.init("neoforge");

		if (dist == Dist.CLIENT)
			initClient(bus);
	}

	public void initClient(IEventBus bus) {
		ClientEvents.registerModelPredicates((item, name, callback) -> {
			ItemProperties.registerGeneric(
				WanderingWizardry.id(name),
				callback
			);
		});
		bus.addListener(this::registerBlockEntityRenderers);
		bus.addListener(this::registerEntityLayers);
		bus.addListener(this::registerClientReloadListeners);
		WanderingWizardryClient.init();
	}

	private void commonSetup(final FMLCommonSetupEvent event) {
		event.enqueueWork(TerraBlenderInitializer::init);
	}

	@SubscribeEvent
	public void registerToRegistries(RegisterEvent event) {
		ContentInitializer.listenToAll(((registry, id, item) -> {
			event.register(registry.key(), id, item);
		}));
	}

	@SubscribeEvent
	public void registerClientReloadListeners(RegisterClientReloadListenersEvent ev) {
		ev.registerReloadListener(new PackReloader());
	}

	@SubscribeEvent
	public void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		ClientEvents.registerEntityRenderers((type, renderer) -> {
			event.registerBlockEntityRenderer(type.get(), (BlockEntityRendererProvider<? super BlockEntity>) renderer);
		});
	}

	@SubscribeEvent
	public void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		var boatModel = BoatModel.createBodyModel();
		var chestBoatModel = ChestBoatModel.createBodyModel();

		for (var id : BoatComponent.BOATS.keySet()) {
			event.registerLayerDefinition(WanderingWizardryClient.getBoatLayerLocation(id, false), () -> boatModel);
			event.registerLayerDefinition(WanderingWizardryClient.getBoatLayerLocation(id, true), () -> chestBoatModel);
		}

		var signModel = SignRenderer.createSignLayer();
		var hangingSignModel = HangingSignRenderer.createHangingSignLayer();

		for (var id : ModdedSignBlock.SIGNS) {
			event.registerLayerDefinition(WanderingWizardryClient.getSignLayerLocation(id, false), () -> signModel);
			event.registerLayerDefinition(WanderingWizardryClient.getSignLayerLocation(id, true), () -> hangingSignModel);
		}
	}
}
