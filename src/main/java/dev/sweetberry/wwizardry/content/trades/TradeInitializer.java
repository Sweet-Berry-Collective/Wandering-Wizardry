package dev.sweetberry.wwizardry.content.trades;

import dev.sweetberry.wwizardry.content.datagen.DatagenInitializer;
import dev.sweetberry.wwizardry.content.item.ItemInitializer;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.world.entity.npc.VillagerTrades;
import java.util.List;

public class TradeInitializer {
	public static final VillagerTrades.ItemListing[][] WANDERING_TRADER_OFFERS = new VillagerTrades.ItemListing[][] {
		{
			new VillagerTrades.ItemsForEmeralds(
				DatagenInitializer.DENIA_WOOD.SAPLING_ITEM,
				5, 1, 8, 1
			),
			new VillagerTrades.ItemsForEmeralds(
				DatagenInitializer.MYCHA_WOOD.SAPLING_ITEM,
				5, 1, 8, 1
			),
			new VillagerTrades.ItemsForEmeralds(
				ItemInitializer.INDIGO_CAERULEUM,
				1, 1, 12, 1
			),
			new VillagerTrades.ItemsForEmeralds(
				ItemInitializer.SCULKFLOWER,
				1, 1, 12, 1
			)
		},
		{
			new VillagerTrades.ItemsForEmeralds(
				ItemInitializer.CRYSTALLINE_SCULK_SHARD,
				3, 1, 12, 5
			)
		}
	};

	public static void init() {
		addWanderingTradesFor(1);
		addWanderingTradesFor(2);
	}

	private static void addWanderingTradesFor(int level) {
		TradeOfferHelper.registerWanderingTraderOffers(
			level,
			offers -> offers.addAll(List.of(WANDERING_TRADER_OFFERS[level-1]))
		);
	}
}
