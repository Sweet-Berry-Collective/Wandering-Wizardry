package io.github.sweetberrycollective.wwizardry.block;

import com.terraformersmc.terraform.sign.block.TerraformSignBlock;
import com.terraformersmc.terraform.sign.block.TerraformWallSignBlock;
import io.github.sweetberrycollective.wwizardry.WanderingMod;
import net.minecraft.block.*;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.SignType;
import net.minecraft.util.math.Direction;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;

import java.util.HashMap;
import java.util.Map;

public class WoodType {
	public final Block LOG;
	public final Block STRIPPED_LOG;
	public final Block WOOD;
	public final Block STRIPPED_WOOD;
	public final Block PLANKS;
	public final Block STAIRS;
	public final Block SLAB;
	public final Block BUTTON;
	public final Block PRESSURE_PLATE;
	public final Block DOOR;
	public final Block TRAPDOOR;
	public final Block SIGN;
	public final Block SIGN_WALL;
	public final Block FENCE;
	public final Block FENCE_GATE;

	public WoodType(String baseName, MapColor wood, MapColor bark) {
		LOG = WanderingBlocks.registerBlock(baseName+"_log", createLogBlock(bark, wood));
		STRIPPED_LOG = WanderingBlocks.registerBlock("stripped_"+baseName+"_log", createLogBlock(wood, wood));
		WOOD = WanderingBlocks.registerBlock(baseName+"_wood", createLogBlock(bark, wood));
		STRIPPED_WOOD = WanderingBlocks.registerBlock("stripped_"+baseName+"_wood", createLogBlock(wood, wood));
		PLANKS = WanderingBlocks.registerBlock(baseName+"_planks", new Block(QuiltBlockSettings.of(Material.WOOD)));
		STAIRS = WanderingBlocks.registerBlock(baseName+"_stairs", new StairsBlock(PLANKS.getDefaultState(), QuiltBlockSettings.of(Material.WOOD)));
		SLAB = WanderingBlocks.registerBlock(baseName+"_slab", new SlabBlock(QuiltBlockSettings.of(Material.WOOD)));
		BUTTON = WanderingBlocks.registerBlock(baseName+"_slab", new WoodenButtonBlock(QuiltBlockSettings.of(Material.WOOD)));
		PRESSURE_PLATE = WanderingBlocks.registerBlock(baseName+"_slab", new PressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, QuiltBlockSettings.of(Material.WOOD)));
		DOOR = WanderingBlocks.registerBlock(baseName+"_slab", new DoorBlock(QuiltBlockSettings.of(Material.WOOD)));
		TRAPDOOR = WanderingBlocks.registerBlock(baseName+"_slab", new TrapdoorBlock(QuiltBlockSettings.of(Material.WOOD)));
		SIGN = WanderingBlocks.registerBlock(baseName+"_slab", new TerraformSignBlock(WanderingMod.id("entity/sign/"+baseName), QuiltBlockSettings.of(Material.WOOD)));
		SIGN_WALL = WanderingBlocks.registerBlock(baseName+"_slab", new TerraformWallSignBlock(WanderingMod.id("entity/sign/"+baseName), QuiltBlockSettings.of(Material.WOOD)));
		FENCE = WanderingBlocks.registerBlock(baseName+"_slab", new FenceBlock(QuiltBlockSettings.of(Material.WOOD)));
		FENCE_GATE = WanderingBlocks.registerBlock(baseName+"_slab", new FenceGateBlock(QuiltBlockSettings.of(Material.WOOD)));
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
