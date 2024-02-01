package dev.sweetberry.wwizardry.content.item;

import com.terraformersmc.terraform.boat.api.TerraformBoatType;
import com.terraformersmc.terraform.boat.api.item.TerraformBoatItemHelper;
import com.terraformersmc.terraform.boat.impl.item.TerraformBoatItem;
import dev.sweetberry.wwizardry.Mod;
import dev.sweetberry.wwizardry.api.registry.RegistryContext;
import dev.sweetberry.wwizardry.content.block.*;
import dev.sweetberry.wwizardry.content.block.altar.AltarCatalyzerBlock;
import dev.sweetberry.wwizardry.content.block.altar.AltarPedestalBlock;
import dev.sweetberry.wwizardry.content.block.nature.SculkflowerBlock;
import dev.sweetberry.wwizardry.content.block.redstone.ResonatorBlock;
import dev.sweetberry.wwizardry.content.item.charm.BrewingCharmItem;
import dev.sweetberry.wwizardry.content.item.charm.CraftingCharmItem;
import dev.sweetberry.wwizardry.content.sounds.SoundInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.RecordItem;
import java.util.ArrayList;
import java.util.List;

public class ItemInitializer {
	public static final RegistryContext<Item> ITEMS = new RegistryContext<>(BuiltInRegistries.ITEM);
	public static final List<ItemStack> STACKS = new ArrayList<>();

	public static final Item CRYSTALLINE_SCULK_SHARD = registerItem(
			"crystalline_sculk",
			new Item(
					new Item.Properties()
			)
	);

	public static final Item CRYSTALLINE_SCULK_BLOCK = registerItem(
			"crystalline_sculk_block",
			new BlockItem(
					CrystalSculkBlock.INSTANCE,
					new Item.Properties()
			)
	);

	public static final Item SCULKFLOWER = registerItem(
			"sculkflower",
			new BlockItem(
					SculkflowerBlock.INSTANCE,
					new Item.Properties()
			)
	);

	public static final Item INDIGO_CAERULEUM = registerItem(
		"indigo_caeruleum",
		new BlockItem(
			BlockInitializer.INDIGO_CAERULEUM,
			new Item.Properties()
		)
	);

	public static final Item MYCHA_ROOTS = registerItem(
		"mycha_roots",
		new BlockItem(
			BlockInitializer.MYCHA_ROOTS,
			 new Item.Properties()
		)
	);

	public static final Item CAMERA = registerItem(
		"camera",
		new BlockItem(
			CameraBlock.INSTANCE,
			new Item.Properties()
		)
	);

	public static final Item REINFORCED_GLASS = registerItem(
		"reinforced_glass",
		new BlockItem(
			BlockInitializer.REINFORCED_GLASS,
			new Item.Properties()
		)
	);

	public static final Item REINFORCED_GLASS_PANE = registerItem(
		"reinforced_glass_pane",
		new BlockItem(
			BlockInitializer.REINFORCED_GLASS_PANE,
			new Item.Properties()
		)
	);

	public static final Item REDSTONE_LANTERN = registerItem(
		"redstone_lantern",
		new BlockItem(
			BlockInitializer.REDSTONE_LANTERN,
			new Item.Properties()
		)
	);

	public static final Item WALL_HOLDER_EMPTY = registerItem(
		"wall_holder",
		new BlockItem(
			WallHolderBlock.EMPTY,
			new Item.Properties()
		)
	);

	public static final Item ROSE_QUARTZ = registerItem(
		"rose_quartz",
		new Item(
			new Item.Properties()
		)
	);
	public static final Item ROSE_QUARTZ_ORE = registerItem(
		"rose_quartz_ore",
		new BlockItem(
			BlockInitializer.ROSE_QUARTZ_ORE,
			new Item.Properties()
		)
	);
	public static final Item DEEPSLATE_ROSE_QUARTZ_ORE = registerItem(
		"deepslate_rose_quartz_ore",
		new BlockItem(
			BlockInitializer.DEEPSLATE_ROSE_QUARTZ_ORE,
			new Item.Properties()
		)
	);
	public static final Item ROSE_QUARTZ_BLOCK = registerItem(
		"rose_quartz_block",
		new BlockItem(
			BlockInitializer.ROSE_QUARTZ_BLOCK,
			new Item.Properties()
		)
	);

	public static final Item MYCELIAL_SAND = registerItem(
		"mycelial_sand",
		new BlockItem(
			BlockInitializer.MYCELIAL_SAND,
			new Item.Properties()
		)
	);

	public static final Item MODULO_COMPARATOR = registerItem(
		"modulo_comparator",
		new BlockItem(
			BlockInitializer.MODULO_COMPARATOR,
			new Item.Properties()
		)
	);

	public static final Item REDSTONE_STEPPER = registerItem(
		"redstone_stepper",
		new BlockItem(
			BlockInitializer.REDSTONE_STEPPER,
			new Item.Properties()
		)
	);

	public static final Item RESONATOR = registerItem(
		"sculk_resonator",
		new BlockItem(
			ResonatorBlock.INSTANCE,
			new Item.Properties()
		)
	);

	public static final Item SLOT_CHARM = registerItem(
		"slot_charm",
		new SelfRemainderingItem(
			new Item.Properties()
				.stacksTo(1)
		)
	);

	public static final Item CRAFTING_CHARM = registerItem(
		"crafting_charm",
		new CraftingCharmItem(
			new Item.Properties()
				.stacksTo(1)
		)
	);

	public static final Item BREWING_CHARM = registerItem(
		"brewing_charm",
		new BrewingCharmItem(
			new Item.Properties()
				.stacksTo(1)
		)
	);

	public static final Item MUSIC_DISC_WANDERING = registerItem(
		"music_disc_wandering",
		new RecordItem(
			10,
			SoundInitializer.DISC_WANDERING,
			new Item.Properties()
				.stacksTo(1)
				.rarity(Rarity.RARE),
			140
		)
	);

	public static final CreativeModeTab GROUP = FabricItemGroup.builder()
			.icon(CRYSTALLINE_SCULK_SHARD::getDefaultInstance)
			.displayItems((display, collector) -> collector.acceptAll(STACKS))
			.title(Component.translatable("itemGroup.wwizardry.items"))
			.build();

	public static void init() {
		registerItem("void_bag", VoidBagItem.INSTANCE);
		registerItem("soul_mirror", SoulMirrorItem.INSTANCE);

		registerItem("altar_pedestal", AltarPedestalBlock.ITEM);
		registerItem("altar_catalyzer", AltarCatalyzerBlock.ITEM);

		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Mod.id("items"), GROUP);
	}

	public static Item registerItem(String id, Item item) {
		STACKS.add(new ItemStack(item));
		return ITEMS.register(Mod.id(id), item);
	}

	public static Item registerBoatItem(String id, ResourceKey<TerraformBoatType> boatTypeKey, boolean chest, Item.Properties itemSettings) {
		// TODO: Replace this with an in-house thing
		var item = registerItem(id, new TerraformBoatItem(boatTypeKey, chest, itemSettings));
		TerraformBoatItemHelper.registerBoatDispenserBehavior(item, boatTypeKey, chest);
		return item;
	}
}
