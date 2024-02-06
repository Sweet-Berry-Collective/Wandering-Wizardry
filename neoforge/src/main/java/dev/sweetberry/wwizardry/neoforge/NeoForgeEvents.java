package dev.sweetberry.wwizardry.neoforge;

import com.mojang.datafixers.util.Either;
import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.client.WanderingWizardryClient;
import dev.sweetberry.wwizardry.client.content.events.ItemTooltipHandler;
import dev.sweetberry.wwizardry.client.content.events.ModelPredicates;
import dev.sweetberry.wwizardry.client.content.events.PackReloader;
import dev.sweetberry.wwizardry.content.events.UseBlockHandler;
import dev.sweetberry.wwizardry.neoforge.component.NeoForgeComponents;
import dev.sweetberry.wwizardry.neoforge.networking.ComponentSyncPayload;
import dev.sweetberry.wwizardry.neoforge.networking.NeoForgeNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.entity.EntityEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.BabyEntitySpawnEvent;
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class NeoForgeEvents {
	public static List<Entity> addedEntities = new ArrayList<>();

	public static void init() {
		NeoForge.EVENT_BUS.register(NeoForgeEvents.class);
	}

	@SubscribeEvent
	public static void onJoinLevel(EntityJoinLevelEvent event) {
		if (!event.getLevel().isClientSide)
			return;
		addedEntities.add(event.getEntity());
	}

	@SubscribeEvent
	public static void onTooltip(RenderTooltipEvent.GatherComponents event) {
		var lines = event.getTooltipElements();

		ItemTooltipHandler.addTooltips(
			event.getItemStack(), TooltipFlag.NORMAL, (i, c)
				-> lines.addAll(
					i,
				(Collection<? extends Either<FormattedText, TooltipComponent>>) (Object)
					c.stream().map(Either::left).toList()
			)
		);
	}

	@SubscribeEvent
	public static void onClientTick(TickEvent.ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.END)
			return;
		WanderingWizardryClient.tickCounter++;
		for (var entity : addedEntities) {
			NeoForgeComponents.COMPONENTS.forEach((key, it) -> {
				var data = entity.getData(it);
				PacketDistributor.SERVER.noArg().send(new ComponentSyncPayload(entity, key, data.component));
			});
		}
		addedEntities.clear();
	}

	@SubscribeEvent
	public static void onUseOnBlock(UseItemOnBlockEvent event) {
		var result = UseBlockHandler.onBlockUse(
			event.getEntity(),
			event.getLevel(),
			event.getHand(),
			event.getPos(),
			event.getFace()
		);
		if (result == InteractionResult.PASS)
			return;
		event.cancelWithResult(result);
	}
}
