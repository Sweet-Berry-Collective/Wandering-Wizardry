package dev.sweetberry.wwizardry.content.item;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.api.Lazy;
import dev.sweetberry.wwizardry.api.registry.RegistryContext;
import dev.sweetberry.wwizardry.content.block.BlockInitializer;
import dev.sweetberry.wwizardry.content.block.WallHolderBlock;
import dev.sweetberry.wwizardry.content.block.altar.AltarCatalyzerBlock;
import dev.sweetberry.wwizardry.content.block.altar.AltarPedestalBlock;
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
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ItemInitializer {
	public static final RegistryContext<Item> ITEMS = new RegistryContext<>(BuiltInRegistries.ITEM);
	public static final RegistryContext<CreativeModeTab> TABS = new RegistryContext<>(BuiltInRegistries.CREATIVE_MODE_TAB);
	public static final List<Lazy<Item>> STACKS = new ArrayList<>();

	public static final Lazy<Item> CRYSTALLINE_SCULK_SHARD = registerItem(
		"crystalline_sculk",
		() -> new Item(
				new Item.Properties()
		)
	);

	public static final Lazy<Item> CRYSTALLINE_SCULK_BLOCK = registerItem(
		"crystalline_sculk_block",
		() -> new BlockItem(
			BlockInitializer.CRYSTALLINE_SCULK.get(),
			new Item.Properties()
		)
	);

	public static final Lazy<Item> SCULKFLOWER = registerItem(
		"sculkflower",
		() -> new BlockItem(
			BlockInitializer.SCULKFLOWER.get(),
			new Item.Properties()
		)
	);

	public static final Lazy<Item> INDIGO_CAERULEUM = registerItem(
		"indigo_caeruleum",
		() -> new BlockItem(
			BlockInitializer.INDIGO_CAERULEUM.get(),
			new Item.Properties()
		)
	);

	public static final Lazy<Item> MYCHA_ROOTS = registerItem(
		"mycha_roots",
		() -> new BlockItem(
			BlockInitializer.MYCHA_ROOTS.get(),
			 new Item.Properties()
		)
	);

	public static final Lazy<Item> CAMERA = registerItem(
		"camera",
		() -> new BlockItem(
			BlockInitializer.CAMERA.get(),
			new Item.Properties()
		)
	);

	public static final Lazy<Item> REINFORCED_GLASS = registerItem(
		"reinforced_glass",
		() -> new BlockItem(
			BlockInitializer.REINFORCED_GLASS.get(),
			new Item.Properties()
		)
	);

	public static final Lazy<Item> REINFORCED_GLASS_PANE = registerItem(
		"reinforced_glass_pane",
		() -> new BlockItem(
			BlockInitializer.REINFORCED_GLASS_PANE.get(),
			new Item.Properties()
		)
	);

	public static final Lazy<Item> REDSTONE_LANTERN = registerItem(
		"redstone_lantern",
		() -> new BlockItem(
			BlockInitializer.REDSTONE_LANTERN.get(),
			new Item.Properties()
		)
	);

	public static final Lazy<Item> WALL_HOLDER_EMPTY = registerItem(
		"wall_holder",
		() -> new BlockItem(
			BlockInitializer.WALL_HOLDER.get(),
			new Item.Properties()
		)
	);

	public static final Lazy<Item> ROSE_QUARTZ = registerItem(
		"rose_quartz",
		() -> new Item(
			new Item.Properties()
		)
	);
	public static final Lazy<Item> ROSE_QUARTZ_ORE = registerItem(
		"rose_quartz_ore",
		() -> new BlockItem(
			BlockInitializer.ROSE_QUARTZ_ORE.get(),
			new Item.Properties()
		)
	);
	public static final Lazy<Item> DEEPSLATE_ROSE_QUARTZ_ORE = registerItem(
		"deepslate_rose_quartz_ore",
		() -> new BlockItem(
			BlockInitializer.DEEPSLATE_ROSE_QUARTZ_ORE.get(),
			new Item.Properties()
		)
	);
	public static final Lazy<Item> ROSE_QUARTZ_BLOCK = registerItem(
		"rose_quartz_block",
		() -> new BlockItem(
			BlockInitializer.ROSE_QUARTZ_BLOCK.get(),
			new Item.Properties()
		)
	);

	public static final Lazy<Item> MYCELIAL_SAND = registerItem(
		"mycelial_sand",
		() -> new BlockItem(
			BlockInitializer.MYCELIAL_SAND.get(),
			new Item.Properties()
		)
	);

	public static final Lazy<Item> MODULO_COMPARATOR = registerItem(
		"modulo_comparator",
		() -> new BlockItem(
			BlockInitializer.MODULO_COMPARATOR.get(),
			new Item.Properties()
		)
	);

	public static final Lazy<Item> REDSTONE_STEPPER = registerItem(
		"redstone_stepper",
		() -> new BlockItem(
			BlockInitializer.REDSTONE_STEPPER.get(),
			new Item.Properties()
		)
	);

	public static final Lazy<Item> RESONATOR = registerItem(
		"sculk_resonator",
		() -> new BlockItem(
			BlockInitializer.SCULK_RESONATOR.get(),
			new Item.Properties()
		)
	);

	public static final Lazy<Item> SLOT_CHARM = registerItem(
		"slot_charm",
		() -> new SelfRemainderingItem(
			new Item.Properties()
				.stacksTo(1)
		)
	);

	public static final Lazy<Item> CRAFTING_CHARM = registerItem(
		"crafting_charm",
		() -> new CraftingCharmItem(
			new Item.Properties()
				.stacksTo(1)
		)
	);

	public static final Lazy<Item> BREWING_CHARM = registerItem(
		"brewing_charm",
		() -> new BrewingCharmItem(
			new Item.Properties()
				.stacksTo(1)
		)
	);

	public static final Lazy<Item> SMITHING_CHARM = registerItem(
		"smithing_charm",
		() -> new SmithingCharmItem(
			new Item.Properties()
				.stacksTo(1)
		)
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
		)
	);

	public static final Lazy<Item> VOID_BAG = registerItem(
		"void_bag",
		() -> new VoidBagItem(
			new Item.Properties()
				.stacksTo(1)
		)
	);

	public static final Lazy<SoulMirrorItem> SOUL_MIRROR = registerItem(
		"soul_mirror",
		() -> new SoulMirrorItem(
			new Item.Properties()
				.stacksTo(1)
		)
	);

	public static final Lazy<Item> ALTAR_PEDESTAL = registerItem(
		"altar_pedestal",
		() -> new BlockItem(
			BlockInitializer.ALTAR_PEDESTAL.get(),
			new Item.Properties()
		)
	);

	public static final Lazy<Item> ALTAR_CATALYZER = registerItem(
		"altar_catalyzer",
		() -> new BlockItem(
			BlockInitializer.ALTAR_CATALYZER.get(),
			new Item.Properties()
		)
	);

	public static <T extends Item> Lazy<T> registerItem(String id, Supplier<T> item) {
		var lazy = ITEMS.register(WanderingWizardry.id(id), (Supplier<Item>)item);
		STACKS.add(lazy);
		return (Lazy<T>) lazy;
	}

	public static Lazy<Item> registerBoatItem(String id, ResourceLocation type, boolean chest, Item.Properties itemSettings) {
        return registerItem(id, () -> new ModdedBoatItem(type, chest, itemSettings));
	}

	public static Lazy<CreativeModeTab> registerTab(String id, Supplier<CreativeModeTab> tab) {
		return TABS.register(WanderingWizardry.id(id), tab);
	}
}
