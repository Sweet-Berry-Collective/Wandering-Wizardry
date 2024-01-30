package dev.sweetberry.wwizardry.content.item;

import com.terraformersmc.terraform.boat.api.TerraformBoatType;
import com.terraformersmc.terraform.boat.api.item.TerraformBoatItemHelper;
import dev.sweetberry.wwizardry.Mod;
import dev.sweetberry.wwizardry.content.block.*;
import dev.sweetberry.wwizardry.content.block.altar.AltarCatalyzerBlock;
import dev.sweetberry.wwizardry.content.block.altar.AltarPedestalBlock;
import dev.sweetberry.wwizardry.content.block.nature.SculkflowerBlock;
import dev.sweetberry.wwizardry.content.block.redstone.ResonatorBlock;
import dev.sweetberry.wwizardry.content.item.charm.BrewingCharmItem;
import dev.sweetberry.wwizardry.content.item.charm.CraftingCharmItem;
import dev.sweetberry.wwizardry.content.sounds.SoundInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Rarity;

import java.util.ArrayList;
import java.util.List;

public class ItemInitializer {
	static {
		STACKS = new ArrayList<>();
	}

	public static final Item CRYSTALLINE_SCULK_SHARD = registerItem(
			"crystalline_sculk",
			new Item(
					new FabricItemSettings()
			)
	);

	public static final Item CRYSTALLINE_SCULK_BLOCK = registerItem(
			"crystalline_sculk_block",
			new BlockItem(
					CrystalSculkBlock.INSTANCE,
					new FabricItemSettings()
			)
	);

	public static final Item SCULKFLOWER = registerItem(
			"sculkflower",
			new BlockItem(
					SculkflowerBlock.INSTANCE,
					new FabricItemSettings()
			)
	);

	public static final Item INDIGO_CAERULEUM = registerItem(
		"indigo_caeruleum",
		new BlockItem(
			BlockInitializer.INDIGO_CAERULEUM,
			new FabricItemSettings()
		)
	);

	public static final Item MYCHA_ROOTS = registerItem(
		"mycha_roots",
		new BlockItem(
			BlockInitializer.MYCHA_ROOTS,
			 new FabricItemSettings()
		)
	);

	public static final Item CAMERA = registerItem(
		"camera",
		new BlockItem(
			CameraBlock.INSTANCE,
			new FabricItemSettings()
		)
	);

	public static final Item REINFORCED_GLASS = registerItem(
		"reinforced_glass",
		new BlockItem(
			BlockInitializer.REINFORCED_GLASS,
			new FabricItemSettings()
		)
	);

	public static final Item REINFORCED_GLASS_PANE = registerItem(
		"reinforced_glass_pane",
		new BlockItem(
			BlockInitializer.REINFORCED_GLASS_PANE,
			new FabricItemSettings()
		)
	);

	public static final Item REDSTONE_LANTERN = registerItem(
		"redstone_lantern",
		new BlockItem(
			BlockInitializer.REDSTONE_LANTERN,
			new FabricItemSettings()
		)
	);

	public static final Item WALL_HOLDER_EMPTY = registerItem(
		"wall_holder",
		new BlockItem(
			WallHolderBlock.EMPTY,
			new FabricItemSettings()
		)
	);

	public static final Item ROSE_QUARTZ = registerItem(
		"rose_quartz",
		new Item(new FabricItemSettings())
	);
	public static final Item ROSE_QUARTZ_ORE = registerItem(
		"rose_quartz_ore",
		new BlockItem(
			BlockInitializer.ROSE_QUARTZ_ORE,
			new FabricItemSettings()
		)
	);
	public static final Item DEEPSLATE_ROSE_QUARTZ_ORE = registerItem(
		"deepslate_rose_quartz_ore",
		new BlockItem(
			BlockInitializer.DEEPSLATE_ROSE_QUARTZ_ORE,
			new FabricItemSettings()
		)
	);
	public static final Item ROSE_QUARTZ_BLOCK = registerItem(
		"rose_quartz_block",
		new BlockItem(
			BlockInitializer.ROSE_QUARTZ_BLOCK,
			new FabricItemSettings()
		)
	);

	public static final Item MYCELIAL_SAND = registerItem(
		"mycelial_sand",
		new BlockItem(
			BlockInitializer.MYCELIAL_SAND,
			new FabricItemSettings()
		)
	);

	public static final Item MODULO_COMPARATOR = registerItem(
		"modulo_comparator",
		new BlockItem(
			BlockInitializer.MODULO_COMPARATOR,
			new FabricItemSettings()
		)
	);

	public static final Item REDSTONE_STEPPER = registerItem(
		"redstone_stepper",
		new BlockItem(
			BlockInitializer.REDSTONE_STEPPER,
			new FabricItemSettings()
		)
	);

	public static final Item RESONATOR = registerItem(
		"sculk_resonator",
		new BlockItem(
			ResonatorBlock.INSTANCE,
			new FabricItemSettings()
		)
	);

	public static final Item SLOT_CHARM = registerItem(
		"slot_charm",
		new SelfRemainderingItem(
			new FabricItemSettings()
				.maxCount(1)
		)
	);

	public static final Item CRAFTING_CHARM = registerItem(
		"crafting_charm",
		new CraftingCharmItem(
			new FabricItemSettings()
				.maxCount(1)
		)
	);

	public static final Item BREWING_CHARM = registerItem(
		"brewing_charm",
		new BrewingCharmItem(
			new FabricItemSettings()
				.maxCount(1)
		)
	);

	public static final Item MUSIC_DISC_WANDERING = registerItem(
		"music_disc_wandering",
		new MusicDiscItem(
			10,
			SoundInitializer.DISC_WANDERING,
			new FabricItemSettings()
				.maxCount(1)
				.rarity(Rarity.RARE),
			140
		)
	);

	public static final List<ItemStack> STACKS;

	// This is here because of 'Illegal forward reference'
	public static ItemStack getIcon() {
		return CRYSTALLINE_SCULK_SHARD.getDefaultStack();
	}

	public static final ItemGroup GROUP = FabricItemGroup.builder()
			.icon(ItemInitializer::getIcon)
			.entries((display, collector) -> collector.addStacks(STACKS))
			.name(Text.translatable("itemGroup.wwizardry.items"))
			.build();

	public static void init() {
		registerItem("void_bag", VoidBagItem.INSTANCE);
		registerItem("soul_mirror", SoulMirrorItem.INSTANCE);

		registerItem("altar_pedestal", AltarPedestalBlock.ITEM);
		registerItem("altar_catalyzer", AltarCatalyzerBlock.ITEM);

		Registry.register(Registries.ITEM_GROUP, Mod.id("items"), GROUP);
	}

	public static Item registerItem(String id, Item item) {
		STACKS.add(new ItemStack(item));
		return Registry.register(Registries.ITEM, Mod.id(id), item);
	}

	public static Item registerBoatItem(String id, RegistryKey<TerraformBoatType> boatTypeKey, boolean chest, FabricItemSettings itemSettings) {
		Item item = TerraformBoatItemHelper.registerBoatItem(Mod.id(id), boatTypeKey, chest, itemSettings);
		STACKS.add(new ItemStack(item));
		return item;
	}
}
