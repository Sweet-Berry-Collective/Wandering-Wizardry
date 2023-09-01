package dev.sweetberry.wwizardry.item;

import com.terraformersmc.terraform.boat.api.TerraformBoatType;
import com.terraformersmc.terraform.boat.api.item.TerraformBoatItemHelper;
import dev.sweetberry.wwizardry.block.*;
import dev.sweetberry.wwizardry.WanderingMod;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

import java.util.ArrayList;
import java.util.List;

public class WanderingItems {
	static {
		STACKS = new ArrayList<>();
	}

	public static final Item CRYSTALLINE_SCULK_SHARD = registerItem(
			"crystalline_sculk",
			new Item(
					new QuiltItemSettings()
			)
	);

	public static final Item CRYSTALLINE_SCULK_BLOCK = registerItem(
			"crystalline_sculk_block",
			new BlockItem(
					CrystalSculkBlock.INSTANCE,
					new QuiltItemSettings()
			)
	);

	public static final Item SCULKFLOWER = registerItem(
			"sculkflower",
			new BlockItem(
					SculkflowerBlock.INSTANCE,
					new QuiltItemSettings()
			)
	);

	public static final Item INDIGO_CAERULEUM = registerItem(
		"indigo_caeruleum",
		new BlockItem(
			WanderingBlocks.INDIGO_CAERULEUM,
			new QuiltItemSettings()
		)
	);

	public static final Item CAMERA = registerItem(
		"camera",
		new BlockItem(
			CameraBlock.INSTANCE,
			new QuiltItemSettings()
		)
	);

	public static final Item REINFORCED_GLASS = registerItem(
		"reinforced_glass",
		new BlockItem(
			WanderingBlocks.REINFORCED_GLASS,
			new QuiltItemSettings()
		)
	);

	public static final Item REINFORCED_GLASS_PANE = registerItem(
		"reinforced_glass_pane",
		new BlockItem(
			WanderingBlocks.REINFORCED_GLASS_PANE,
			new QuiltItemSettings()
		)
	);

	public static final Item REDSTONE_LANTERN = registerItem(
		"redstone_lantern",
		new BlockItem(
			WanderingBlocks.REDSTONE_LANTERN,
			new QuiltItemSettings()
		)
	);

	public static final Item WALL_HOLDER_EMPTY = registerItem(
		"wall_holder",
		new BlockItem(
			WallHolderBlock.EMPTY,
			new QuiltItemSettings()
		)
	);

	public static final Item ROSE_QUARTZ = registerItem(
		"rose_quartz",
		new Item(new QuiltItemSettings())
	);
	public static final Item ROSE_QUARTZ_ORE = registerItem(
		"rose_quartz_ore",
		new BlockItem(
			WanderingBlocks.ROSE_QUARTZ_ORE,
			new QuiltItemSettings()
		)
	);
	public static final Item DEEPSLATE_ROSE_QUARTZ_ORE = registerItem(
		"deepslate_rose_quartz_ore",
		new BlockItem(
			WanderingBlocks.DEEPSLATE_ROSE_QUARTZ_ORE,
			new QuiltItemSettings()
		)
	);
	public static final Item ROSE_QUARTZ_BLOCK = registerItem(
		"rose_quartz_block",
		new BlockItem(
			WanderingBlocks.ROSE_QUARTZ_BLOCK,
			new QuiltItemSettings()
		)
	);

	public static final Item MYCELIAL_SAND = registerItem(
		"mycelial_sand",
		new BlockItem(
			WanderingBlocks.MYCELIAL_SAND,
			new QuiltItemSettings()
		)
	);

	public static final Item MODULO_COMPARATOR = registerItem(
		"modulo_comparator",
		new BlockItem(
			WanderingBlocks.MODULO_COMPARATOR,
			new QuiltItemSettings()
		)
	);

	public static final Item REDSTONE_STEPPER = registerItem(
		"redstone_stepper",
		new BlockItem(
			WanderingBlocks.REDSTONE_STEPPER,
			new QuiltItemSettings()
		)
	);

	public static final Item RESONATOR = registerItem(
		"sculk_resonator",
		new BlockItem(
			ResonatorBlock.INSTANCE,
			new QuiltItemSettings()
		)
	);

	public static final Item SLOT_CHARM = registerItem(
		"slot_charm",
		new Item(
			new QuiltItemSettings()
				.recipeRemainder(((original, recipe) -> original))
				.maxCount(1)
		)
	);

	public static final Item CRAFTING_CHARM = registerItem(
		"crafting_charm",
		new Item(
			new QuiltItemSettings()
				.recipeRemainder(((original, recipe) -> original))
				.maxCount(1)
		)
	);

	public static final List<ItemStack> STACKS;

	// This is here because of 'Illegal forward reference'
	public static ItemStack getIcon() {
		return CRYSTALLINE_SCULK_SHARD.getDefaultStack();
	}

	public static final ItemGroup GROUP = FabricItemGroup.builder()
			.icon(WanderingItems::getIcon)
			.entries((display, collector) -> collector.addStacks(STACKS))
			.name(Text.translatable("itemGroup.wwizardry.items"))
			.build();

	public static void init() {
		registerItem("void_bag", VoidBagItem.INSTANCE);

		registerItem("altar_pedestal", AltarPedestalBlock.ITEM);
		registerItem("altar_catalyzer", AltarCatalyzerBlock.ITEM);

		Registry.register(Registries.ITEM_GROUP, WanderingMod.id("items"), GROUP);
	}

	public static Item registerItem(String id, Item item) {
		STACKS.add(new ItemStack(item));
		return Registry.register(Registries.ITEM, WanderingMod.id(id), item);
	}

	public static Item registerBoatItem(String id, RegistryKey<TerraformBoatType> boatTypeKey, boolean chest, QuiltItemSettings itemSettings) {
		Item item = TerraformBoatItemHelper.registerBoatItem(WanderingMod.id(id), boatTypeKey, chest, itemSettings);
		STACKS.add(new ItemStack(item));
		return item;
	}
}
