package dev.sweetberry.wwizardry.content.trades;

import dev.sweetberry.wwizardry.api.Lazy;
import dev.sweetberry.wwizardry.content.datagen.DatagenInitializer;
import dev.sweetberry.wwizardry.content.item.ItemInitializer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class TradeInitializer {
	public static final VillagerTrades.ItemListing[][] WANDERING_TRADER_OFFERS = new VillagerTrades.ItemListing[][] {
		{
			new ItemsForEmeralds(
				DatagenInitializer.DENIA_WOOD.SAPLING_ITEM,
				5, 1, 8, 1, 0.05f
			),
			new ItemsForEmeralds(
				DatagenInitializer.MYCHA_WOOD.SAPLING_ITEM,
				5, 1, 8, 1, 0.05f
			),
			new ItemsForEmeralds(
				ItemInitializer.INDIGO_CAERULEUM,
				1, 1, 12, 1, 0.05f
			),
			new ItemsForEmeralds(
				ItemInitializer.SCULKFLOWER,
				1, 1, 12, 1, 0.05f
			)
		},
		{
			new ItemsForEmeralds(
				ItemInitializer.CRYSTALLINE_SCULK_SHARD,
				3, 1, 12, 5, 0.05f
			)
		}
	};

	public static class ItemsForEmeralds implements VillagerTrades.ItemListing {
		private final Lazy<Item> item;
		private final int count;
		private final int cost;
		private final int maxUses;
		private final int xp;
		private final float priceMultiplier;

		public ItemsForEmeralds(Supplier<Item> item, int cost, int count, int maxUses, int xp, float priceMultiplier) {
			this.item = Lazy.create(item);
			this.cost = cost;
			this.count = count;
			this.maxUses = maxUses;
			this.xp = xp;
			this.priceMultiplier = priceMultiplier;
		}

		public MerchantOffer getOffer(Entity entity, RandomSource random) {
			return new MerchantOffer(new ItemStack(Items.EMERALD, cost), item.get().getDefaultInstance().copyWithCount(count), maxUses, xp, priceMultiplier);
		}
	}
}
