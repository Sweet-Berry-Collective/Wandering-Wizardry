package io.github.sweetberrycollective.wwizardry.block;

import com.terraformersmc.terraform.sign.block.TerraformSignBlock;
import com.terraformersmc.terraform.sign.block.TerraformWallSignBlock;
import io.github.sweetberrycollective.wwizardry.WanderingMod;
import io.github.sweetberrycollective.wwizardry.item.WanderingItems;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.SignItem;
import net.minecraft.resource.MultiPackResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;
import org.quiltmc.qsl.resource.loader.api.InMemoryResourcePack;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;
import org.quiltmc.qsl.resource.loader.api.ResourcePackRegistrationContext;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class WoodType implements ResourcePackRegistrationContext.Callback {
	public final String BASE_NAME;

	public final Block LOG;
	public final Item LOG_ITEM;
	public final Block STRIPPED_LOG;
	public final Item STRIPPED_LOG_ITEM;
	public final Block WOOD;
	public final Item WOOD_ITEM;
	public final Block STRIPPED_WOOD;
	public final Item STRIPPED_WOOD_ITEM;
	public final Block PLANKS;
	public final Item PLANKS_ITEM;
	public final Block STAIRS;
	public final Item STAIRS_ITEM;
	public final Block SLAB;
	public final Item SLAB_ITEM;
	public final Block BUTTON;
	public final Item BUTTON_ITEM;
	public final Block PRESSURE_PLATE;
	public final Item PRESSURE_PLATE_ITEM;
	public final Block DOOR;
	public final Item DOOR_ITEM;
	public final Block TRAPDOOR;
	public final Item TRAPDOOR_ITEM;
	public final Block SIGN;
	public final Block SIGN_WALL;
	public final Item SIGN_ITEM;
	public final Block FENCE;
	public final Item FENCE_ITEM;
	public final Block FENCE_GATE;
	public final Item FENCE_GATE_ITEM;

	public WoodType(String baseName, MapColor wood, MapColor bark) {
		BASE_NAME = baseName;

		final var blockSettings = QuiltBlockSettings.of(Material.WOOD).mapColor(wood);
		final var itemSettings = new QuiltItemSettings();

		LOG = WanderingBlocks.registerBlock(baseName+"_log", createLogBlock(bark, wood));
		LOG_ITEM = WanderingItems.registerItem(baseName+"_log", new BlockItem(LOG, itemSettings));

		STRIPPED_LOG = WanderingBlocks.registerBlock("stripped_"+baseName+"_log", createLogBlock(wood, wood));
		STRIPPED_LOG_ITEM = WanderingItems.registerItem("stripped_"+baseName+"_log", new BlockItem(STRIPPED_LOG, itemSettings));

		WOOD = WanderingBlocks.registerBlock(baseName+"_wood", createLogBlock(bark, wood));
		WOOD_ITEM = WanderingItems.registerItem(baseName+"_wood", new BlockItem(WOOD, itemSettings));

		STRIPPED_WOOD = WanderingBlocks.registerBlock("stripped_"+baseName+"_wood", createLogBlock(wood, wood));
		STRIPPED_WOOD_ITEM = WanderingItems.registerItem("stripped_"+baseName+"_wood", new BlockItem(STRIPPED_WOOD, itemSettings));

		PLANKS = WanderingBlocks.registerBlock(baseName+"_planks", new Block(blockSettings));
		PLANKS_ITEM = WanderingItems.registerItem(baseName+"_planks", new BlockItem(PLANKS, itemSettings));

		STAIRS = WanderingBlocks.registerBlock(baseName+"_stairs", new StairsBlock(PLANKS.getDefaultState(), blockSettings));
		STAIRS_ITEM = WanderingItems.registerItem(baseName+"_stairs", new BlockItem(STAIRS, itemSettings));

		SLAB = WanderingBlocks.registerBlock(baseName+"_slab", new SlabBlock(blockSettings));
		SLAB_ITEM = WanderingItems.registerItem(baseName+"_slab", new BlockItem(SLAB, itemSettings));

		BUTTON = WanderingBlocks.registerBlock(baseName+"_button", new WoodenButtonBlock(blockSettings));
		BUTTON_ITEM = WanderingItems.registerItem(baseName+"_button", new BlockItem(BUTTON, itemSettings));

		PRESSURE_PLATE = WanderingBlocks.registerBlock(baseName+"_pressure_plate", new PressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, blockSettings));
		PRESSURE_PLATE_ITEM = WanderingItems.registerItem(baseName+"_pressure_plate", new BlockItem(PRESSURE_PLATE, itemSettings));

		DOOR = WanderingBlocks.registerBlock(baseName+"_door", new DoorBlock(blockSettings));
		DOOR_ITEM = WanderingItems.registerItem(baseName+"_door", new BlockItem(DOOR, itemSettings));

		TRAPDOOR = WanderingBlocks.registerBlock(baseName+"_trapdoor", new TrapdoorBlock(blockSettings));
		TRAPDOOR_ITEM = WanderingItems.registerItem(baseName+"_trapdoor", new BlockItem(TRAPDOOR, itemSettings));

		SIGN = WanderingBlocks.registerBlock(baseName+"_sign", new TerraformSignBlock(WanderingMod.id("entity/sign/"+baseName), blockSettings));
		SIGN_WALL = WanderingBlocks.registerBlock(baseName+"_wall_sign", new TerraformWallSignBlock(WanderingMod.id("entity/sign/"+baseName), blockSettings));
		SIGN_ITEM = WanderingItems.registerItem(baseName+"_sign", new SignItem(itemSettings, SIGN, SIGN_WALL));

		FENCE = WanderingBlocks.registerBlock(baseName+"_fence", new FenceBlock(blockSettings));
		FENCE_ITEM = WanderingItems.registerItem(baseName+"_fence", new BlockItem(FENCE, itemSettings));

		FENCE_GATE = WanderingBlocks.registerBlock(baseName+"_fence_gate", new FenceGateBlock(blockSettings));
		FENCE_GATE_ITEM = WanderingItems.registerItem(baseName+"_fence_gate", new BlockItem(FENCE_GATE, itemSettings));

		ResourceLoader.get(ResourceType.CLIENT_RESOURCES).getRegisterDefaultResourcePackEvent().register(this);
	}

	private static PillarBlock createLogBlock(MapColor top, MapColor side) {
		return new PillarBlock(QuiltBlockSettings.of(
				Material.WOOD,
				(state) ->
						state.get(PillarBlock.AXIS) == Direction.Axis.Y ? top : side)
							.strength(2.0F)
							.sounds(BlockSoundGroup.WOOD)
		);
	}

	@Override
	public void onRegisterPack(@NotNull ResourcePackRegistrationContext context) {
		var manager = context.resourceManager();
		if (!(manager instanceof MultiPackResourceManager multiManager)) return;
		var pack = new InMemoryResourcePack.Named("AutoSlab resources");
		var blockstates = new BlockstateData(context, pack, BASE_NAME);
		blockstates.addToResourcePack();
		context.addResourcePack(pack);
	}

	public static class BlockstateData {
		public final String BUTTON;
		public final String DOOR;
		public final String FENCE;
		public final String FENCE_GATE;
		public final String LOG;
		public final String PLANKS;
		public final String PRESSURE_PLATE;
		public final String SIGN;
		public final String SLAB;
		public final String STAIRS;
		public final String STRIPPED_LOG;
		public final String STRIPPED_WOOD;
		public final String TRAPDOOR;
		public final String WOOD;

		public final @NotNull ResourcePackRegistrationContext context;
		public final InMemoryResourcePack.Named pack;
		public final String baseName;

		public BlockstateData(@NotNull ResourcePackRegistrationContext context, InMemoryResourcePack.Named pack, String baseName) {
			this.context = context;
			this.pack = pack;
			this.baseName = baseName;

			BUTTON = getResource("button");
			DOOR = getResource("door");
			FENCE = getResource("fence");
			FENCE_GATE = getResource("fence_gate");
			LOG = getResource("log");
			PLANKS = getResource("planks");
			PRESSURE_PLATE = getResource("pressure_plate");
			SIGN = getResource("sign");
			SLAB = getResource("slab");
			STAIRS = getResource("stairs");
			STRIPPED_LOG = getResource("stripped_log");
			STRIPPED_WOOD = getResource("stripped_wood");
			TRAPDOOR = getResource("trapdoor");
			WOOD = getResource("wood");
		}


		private String getResource(String name) {
			try {
				return new String(context.resourceManager().open(path(name)).readAllBytes(), StandardCharsets.UTF_8);
			} catch (IOException e) {
				WanderingMod.LOGGER.error("Unable to find "+name);
				return "{}";
			}
		}

		private Identifier path(String file) {
			return WanderingMod.id("datagen/blockstates/woodtype/"+file+".json");
		}

		public void addToResourcePack() {
			put(baseName+"_button", BUTTON);
			put(baseName+"_door", DOOR);
			put(baseName+"_fence", FENCE);
			put(baseName+"_fence_gate", FENCE_GATE);
			put(baseName+"_log", LOG);
			put(baseName+"_planks", PLANKS);
			put(baseName+"_pressure_plate", PRESSURE_PLATE);
			put(baseName+"_sign", SIGN);
			put(baseName+"_wall_sign", SIGN);
			put(baseName+"_slab", SLAB);
			put(baseName+"_stairs", STAIRS);
			put("stripped_"+baseName+"_log", STRIPPED_LOG);
			put("stripped_"+baseName+"_wood", STRIPPED_WOOD);
			put(baseName+"_trapdoor", TRAPDOOR);
			put(baseName+"_wood", WOOD);
		}

		private void put(String path, String text) {
			pack.putText(ResourceType.CLIENT_RESOURCES, WanderingMod.id("blockstates/"+path+".json"), text.replaceAll("%", baseName));
		}
	}
}
