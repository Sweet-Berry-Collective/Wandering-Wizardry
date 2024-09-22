package dev.sweetberry.wwizardry.neoforge;

import com.mojang.datafixers.util.Either;
import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.client.WanderingWizardryClient;
import dev.sweetberry.wwizardry.client.content.events.ClientEvents;
import dev.sweetberry.wwizardry.client.content.events.ItemTooltipHandler;
import dev.sweetberry.wwizardry.client.content.events.PackReloader;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import net.neoforged.neoforge.common.NeoForge;

public class NeoForgeClientEvents {
	public static void init(IEventBus modBus) {
		NeoForge.EVENT_BUS.register(NeoForgeClientEvents.class);

		ClientEvents.registerModelPredicates((item, name, callback) -> {
			ItemProperties.registerGeneric(
				WanderingWizardry.id(name),
				callback
			);
		});
		modBus.addListener(NeoForgeClientEvents::registerEntityRenderers);
		modBus.addListener(NeoForgeClientEvents::registerEntityLayers);
		modBus.addListener(NeoForgeClientEvents::registerClientReloadListeners);
		WanderingWizardryClient.init();
	}

	@SubscribeEvent
	public static void onTooltip(RenderTooltipEvent.GatherComponents event) {
		var lines = event.getTooltipElements();

		ItemTooltipHandler.addTooltips(
			event.getItemStack(),
			TooltipFlag.NORMAL,
			(i, c) -> lines.addAll(i,
				c.stream().map(it -> (FormattedText) it).map(Either::<FormattedText, TooltipComponent>left).toList()
			)
		);
	}

	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Post ev) {
		WanderingWizardryClient.tickCounter++;
	}

	public static void registerClientReloadListeners(RegisterClientReloadListenersEvent ev) {
		ev.registerReloadListener(new PackReloader());
	}

	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		ClientEvents.registerBlockEntityRenderers((type, renderer) -> {
			event.registerBlockEntityRenderer(type.get(), renderer);
		});

		ClientEvents.registerEntityRenderers((type, renderer) -> {
			event.registerEntityRenderer(type.get(), renderer);
		});
	}

	public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		ClientEvents.registerModelLayers(event::registerLayerDefinition);
	}
}
