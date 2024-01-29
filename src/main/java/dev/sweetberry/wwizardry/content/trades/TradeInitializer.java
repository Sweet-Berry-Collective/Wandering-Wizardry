package dev.sweetberry.wwizardry.content.trades;

import dev.sweetberry.wwizardry.content.datagen.DatagenInitializer;
import dev.sweetberry.wwizardry.content.item.ItemInitializer;
import net.minecraft.village.TradeOffers;
import org.quiltmc.qsl.entity.extensions.api.TradeOfferHelper;

import java.util.List;

public class TradeInitializer {

	public static final TradeOffers.Factory[][] WANDERING_TRADER_OFFERS = new TradeOffers.Factory[][] {
		{
			new TradeOffers.SellItemFactory(
				DatagenInitializer.DENIA_WOOD.SAPLING_ITEM,
				5, 1, 8, 1
			),
			new TradeOffers.SellItemFactory(
				DatagenInitializer.MYCHA_WOOD.SAPLING_ITEM,
				5, 1, 8, 1
			),
			new TradeOffers.SellItemFactory(
				ItemInitializer.INDIGO_CAERULEUM,
				1, 1, 12, 1
			),
			new TradeOffers.SellItemFactory(
				ItemInitializer.SCULKFLOWER,
				1, 1, 12, 1
			)
		},
		{
			new TradeOffers.SellItemFactory(
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
