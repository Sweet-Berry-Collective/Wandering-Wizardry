package dev.sweetberry.wwizardry.neoforge;

import com.mojang.datafixers.util.Either;
import dev.sweetberry.wwizardry.client.WanderingWizardryClient;
import dev.sweetberry.wwizardry.client.content.events.ItemTooltipHandler;
import dev.sweetberry.wwizardry.content.events.UseBlockHandler;
import dev.sweetberry.wwizardry.content.villager.VillagerInitializer;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;

import java.util.Arrays;

public class NeoForgeEvents {
	public static void init() {
		NeoForge.EVENT_BUS.register(NeoForgeEvents.class);
	}

	@SubscribeEvent
	public static void onWanderingTrades(WandererTradesEvent ev) {
		ev.getGenericTrades().addAll(Arrays.stream(VillagerInitializer.WANDERING_TRADER_OFFERS[0]).toList());
		ev.getRareTrades().addAll(Arrays.stream(VillagerInitializer.WANDERING_TRADER_OFFERS[1]).toList());
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
		event.cancelWithResult(ItemInteractionResult.FAIL);
	}
}
