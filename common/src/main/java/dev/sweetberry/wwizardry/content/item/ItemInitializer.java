package dev.sweetberry.wwizardry.content.item;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.api.Lazy;
import dev.sweetberry.wwizardry.api.registry.RegistryContext;
import dev.sweetberry.wwizardry.content.block.BlockInitializer;
import dev.sweetberry.wwizardry.content.item.charm.AnvilCharmItem;
import dev.sweetberry.wwizardry.content.item.charm.BrewingCharmItem;
import dev.sweetberry.wwizardry.content.item.charm.CraftingCharmItem;
import dev.sweetberry.wwizardry.content.item.charm.SmithingCharmItem;
import dev.sweetberry.wwizardry.content.sounds.SoundInitializer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.RecordItem;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ItemInitializer {
	public static final RegistryContext<Item> ITEMS = new RegistryContext<>(BuiltInRegistries.ITEM);
	public static final RegistryContext<CreativeModeTab> TABS = new RegistryContext<>(BuiltInRegistries.CREATIVE_MODE_TAB);
	public static final List<Lazy<Item>> ITEMS_STACKS = new ArrayList<>();
	public static final List<Lazy<Item>> BLOCKS_STACKS = new ArrayList<>();

	public static final Lazy<CreativeModeTab> ITEMS_TAB = ItemInitializer.registerTab(
		"items",
		() -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
			.icon(() -> ItemInitializer.CRYSTALLINE_SCULK_SHARD.get().getDefaultInstance())
			.displayItems((display, collector) -> collector.acceptAll(ItemInitializer.ITEMS_STACKS.stream().map(Lazy::get).map(Item::getDefaultInstance).collect(Collectors.toList())))
			.title(net.minecraft.network.chat.Component.translatable("itemGroup.wwizardry.items"))
			.build()
	);

	public static final  Lazy<CreativeModeTab> BLOCKS_TAB = ItemInitializer.registerTab(
		"blocks",
		() -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
			.icon(() -> ItemInitializer.ALTAR_CATALYZER.get().getDefaultInstance())
			.displayItems((display, collector) -> collector.acceptAll(ItemInitializer.BLOCKS_STACKS.stream().map(Lazy::get).map(Item::getDefaultInstance).collect(Collectors.toList())))
			.title(net.minecraft.network.chat.Component.translatable("itemGroup.wwizardry.items"))
			.build()
	);

	public static final Lazy<Item> CRYSTALLINE_SCULK_SHARD = registerItem(
		"crystalline_sculk",
		() -> new Item(
				new Item.Properties()
		),
		ITEMS_STACKS
	);

	public static final Lazy<Item> CRYSTALLINE_SCULK_BLOCK = registerItem(
		"crystalline_sculk_block",
		() -> new BlockItem(
			BlockInitializer.CRYSTALLINE_SCULK.get(),
			new Item.Properties()
		),
		BLOCKS_STACKS
	);

	public static final Lazy<Item> SCULKFLOWER = registerItem(
		"sculkflower",
		() -> new BlockItem(
			BlockInitializer.SCULKFLOWER.get(),
			new Item.Properties()
		),
		BLOCKS_STACKS
	);

	public static final Lazy<Item> INDIGO_CAERULEUM = registerItem(
		"indigo_caeruleum",
		() -> new BlockItem(
			BlockInitializer.INDIGO_CAERULEUM.get(),
			new Item.Properties()
		),
		BLOCKS_STACKS
	);

	public static final Lazy<Item> MYCHA_ROOTS = registerItem(
		"mycha_roots",
		() -> new BlockItem(
			BlockInitializer.MYCHA_ROOTS.get(),
			 new Item.Properties()
		),
		BLOCKS_STACKS
	);

	public static final Lazy<Item> CAMERA = registerItem(
		"camera",
		() -> new BlockItem(
			BlockInitializer.CAMERA.get(),
			new Item.Properties()
		),
		BLOCKS_STACKS
	);

	public static final Lazy<Item> REINFORCED_GLASS = registerItem(
		"reinforced_glass",
		() -> new BlockItem(
			BlockInitializer.REINFORCED_GLASS.get(),
			new Item.Properties()
		),
		BLOCKS_STACKS
	);

	public static final Lazy<Item> REINFORCED_GLASS_PANE = registerItem(
		"reinforced_glass_pane",
		() -> new BlockItem(
			BlockInitializer.REINFORCED_GLASS_PANE.get(),
			new Item.Properties()
		),
		BLOCKS_STACKS
	);

	public static final Lazy<Item> REDSTONE_LANTERN = registerItem(
		"redstone_lantern",
		() -> new BlockItem(
			BlockInitializer.REDSTONE_LANTERN.get(),
			new Item.Properties()
		),
		BLOCKS_STACKS
	);

	public static final Lazy<Item> WALL_HOLDER = registerItem(
		"wall_holder",
		() -> new BlockItem(
			BlockInitializer.WALL_HOLDER.get(),
			new Item.Properties()
		),
		BLOCKS_STACKS
	);

	public static final Lazy<Item> ROSE_QUARTZ = registerItem(
		"rose_quartz",
		() -> new Item(
			new Item.Properties()
		),
		ITEMS_STACKS
	);
	public static final Lazy<Item> ROSE_QUARTZ_ORE = registerItem(
		"rose_quartz_ore",
		() -> new BlockItem(
			BlockInitializer.ROSE_QUARTZ_ORE.get(),
			new Item.Properties()
		),
		BLOCKS_STACKS
	);
	public static final Lazy<Item> DEEPSLATE_ROSE_QUARTZ_ORE = registerItem(
		"deepslate_rose_quartz_ore",
		() -> new BlockItem(
			BlockInitializer.DEEPSLATE_ROSE_QUARTZ_ORE.get(),
			new Item.Properties()
		),
		BLOCKS_STACKS
	);
	public static final Lazy<Item> ROSE_QUARTZ_BLOCK = registerItem(
		"rose_quartz_block",
		() -> new BlockItem(
			BlockInitializer.ROSE_QUARTZ_BLOCK.get(),
			new Item.Properties()
		),
		BLOCKS_STACKS
	);

	public static final Lazy<Item> MYCELIAL_SAND = registerItem(
		"mycelial_sand",
		() -> new BlockItem(
			BlockInitializer.MYCELIAL_SAND.get(),
			new Item.Properties()
		),
		BLOCKS_STACKS
	);

	public static final Lazy<Item> MODULO_COMPARATOR = registerItem(
		"modulo_comparator",
		() -> new BlockItem(
			BlockInitializer.MODULO_COMPARATOR.get(),
			new Item.Properties()
		),
		BLOCKS_STACKS
	);

	public static final Lazy<Item> REDSTONE_STEPPER = registerItem(
		"redstone_stepper",
		() -> new BlockItem(
			BlockInitializer.REDSTONE_STEPPER.get(),
			new Item.Properties()
		),
		BLOCKS_STACKS
	);

	public static final Lazy<Item> RESONATOR = registerItem(
		"sculk_resonator",
		() -> new BlockItem(
			BlockInitializer.SCULK_RESONATOR.get(),
			new Item.Properties()
		),
		BLOCKS_STACKS
	);

	public static final Lazy<Item> SLOT_CHARM = registerItem(
		"slot_charm",
		() -> new SelfRemainderingItem(
			new Item.Properties()
				.stacksTo(1)
		),
		ITEMS_STACKS
	);

	public static final Lazy<Item> CRAFTING_CHARM = registerItem(
		"crafting_charm",
		() -> new CraftingCharmItem(
			new Item.Properties()
				.stacksTo(1)
		),
		ITEMS_STACKS
	);

	public static final Lazy<Item> BREWING_CHARM = registerItem(
		"brewing_charm",
		() -> new BrewingCharmItem(
			new Item.Properties()
				.stacksTo(1)
		),
		ITEMS_STACKS
	);

	public static final Lazy<Item> SMITHING_CHARM = registerItem(
		"smithing_charm",
		() -> new SmithingCharmItem(
			new Item.Properties()
				.stacksTo(1)
		),
		ITEMS_STACKS
	);

	public static final Lazy<Item> ANVIL_CHARM = registerItem(
		"anvil_charm",
		() -> new AnvilCharmItem(
			new Item.Properties()
				.stacksTo(1)
		),
		ITEMS_STACKS
	);

	public static final Lazy<Item> MUSIC_DISC_WANDERING = registerItem(
		"music_disc_wandering",
		() -> new RecordItem(
			10,
			SoundInitializer.DISC_WANDERING.get(),
			new Item.Properties()
				.stacksTo(1)
				.rarity(Rarity.RARE),
			140
		),
		ITEMS_STACKS
	);

	public static final Lazy<Item> VOID_BAG = registerItem(
		"void_bag",
		() -> new VoidBagItem(
			new Item.Properties()
				.stacksTo(1)
		),
		ITEMS_STACKS
	);

	public static final Lazy<SoulMirrorItem> SOUL_MIRROR = registerItem(
		"soul_mirror",
		() -> new SoulMirrorItem(
			new Item.Properties()
				.stacksTo(1)
		),
		ITEMS_STACKS
	);

	public static final Lazy<Item> ALTAR_PEDESTAL = registerItem(
		"altar_pedestal",
		() -> new BlockItem(
			BlockInitializer.ALTAR_PEDESTAL.get(),
			new Item.Properties()
		),
		BLOCKS_STACKS
	);

	public static final Lazy<Item> ALTAR_CATALYZER = registerItem(
		"altar_catalyzer",
		() -> new BlockItem(
			BlockInitializer.ALTAR_CATALYZER.get(),
			new Item.Properties()
		),
		BLOCKS_STACKS
	);

	public static <T extends Item> Lazy<T> registerItem(String id, Supplier<T> item, List<Lazy<Item>> group) {
		var lazy = ITEMS.register(WanderingWizardry.id(id), (Supplier<Item>)item);
		group.add(lazy);
		return (Lazy<T>) lazy;
	}

	public static <T extends Item> Lazy<T> registerItem(String id, Supplier<T> item) {
		return registerItem(id, item, ITEMS_STACKS);
	}

	public static Lazy<Item> registerBoatItem(String id, ResourceLocation type, boolean chest, Item.Properties itemSettings) {
        return registerItem(id, () -> new ModdedBoatItem(type, chest, itemSettings), ITEMS_STACKS);
	}

	public static Lazy<CreativeModeTab> registerTab(String id, Supplier<CreativeModeTab> tab) {
		return TABS.register(WanderingWizardry.id(id), tab);
	}
}
