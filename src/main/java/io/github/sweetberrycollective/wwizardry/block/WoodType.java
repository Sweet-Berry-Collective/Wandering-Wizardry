package io.github.sweetberrycollective.wwizardry.block;

import com.terraformersmc.terraform.sign.block.TerraformSignBlock;
import com.terraformersmc.terraform.sign.block.TerraformWallSignBlock;
import io.github.sweetberrycollective.wwizardry.WanderingMod;
import io.github.sweetberrycollective.wwizardry.item.WanderingItems;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.SignItem;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.Direction;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

public class WoodType {
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
}
