package dev.sweetberry.wwizardry.content.trades;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.api.Lazy;
import dev.sweetberry.wwizardry.content.datagen.DatagenInitializer;
import dev.sweetberry.wwizardry.content.item.ItemInitializer;
import dev.sweetberry.wwizardry.content.map.MapInitializer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.StructureTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

public class TradeInitializer {
	public static final TagKey<Structure> ON_LAB_EXPLORER_MAPS = TagKey.create(Registries.STRUCTURE, WanderingWizardry.id("on_lab_explorer_maps"));
	void a() {
		// cost, destination, displayName, destinationType, maxUsage, villagerXp
		new VillagerTrades.TreasureMapForEmeralds(13, ON_LAB_EXPLORER_MAPS, "filled_map.wwizardry.sculk_lab", MapInitializer.holder(MapInitializer.SCULK_LAB), 12, 5);
	}

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
			),
			new TreasureMapForEmeralds(
				12,
				ON_LAB_EXPLORER_MAPS,
				"filled_map.wwizardry.sculk_lab",
				MapInitializer.SCULK_LAB,
				1,
				5
			)
		}
	};

	public static class TreasureMapForEmeralds implements VillagerTrades.ItemListing {
		private final int emeraldCost;
		private final TagKey<Structure> destination;
		private final String displayName;
		private final Lazy<MapDecorationType> destinationType;
		private final int maxUses;
		private final int villagerXp;

		public TreasureMapForEmeralds(int emeraldCost, TagKey<Structure> destination, String displayName, Lazy<MapDecorationType> destinationType, int maxUses, int villagerXp) {
			this.emeraldCost = emeraldCost;
			this.destination = destination;
			this.displayName = displayName;
			this.destinationType = destinationType;
			this.maxUses = maxUses;
			this.villagerXp = villagerXp;
		}

		@Nullable
		public MerchantOffer getOffer(Entity entity, RandomSource rand) {
			if (!(entity.level() instanceof ServerLevel))
				return null;
			ServerLevel level = (ServerLevel)entity.level();
			BlockPos pos = level.findNearestMapStructure(destination, entity.blockPosition(), 100, true);
			if (pos == null)
				return null;
			ItemStack mapStack = MapItem.create(level, pos.getX(), pos.getZ(), (byte)2, true, true);
			MapItem.renderBiomePreviewMap(level, mapStack);
			MapItemSavedData.addTargetDecoration(mapStack, pos, "+", MapInitializer.MAP_DECORATION_TYPE.holderFor(destinationType));
			mapStack.set(DataComponents.ITEM_NAME, Component.translatable(displayName));
			return new MerchantOffer(new ItemCost(Items.EMERALD, emeraldCost), Optional.of(new ItemCost(Items.COMPASS)), mapStack, maxUses, villagerXp, 0.2F);
		}
	}

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
			return new MerchantOffer(new ItemCost(Items.EMERALD, cost), item.get().getDefaultInstance().copyWithCount(count), maxUses, xp, priceMultiplier);
		}
	}
}
