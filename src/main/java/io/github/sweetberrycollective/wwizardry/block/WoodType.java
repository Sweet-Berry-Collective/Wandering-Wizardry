package io.github.sweetberrycollective.wwizardry.block;

import com.terraformersmc.terraform.sign.block.TerraformSignBlock;
import com.terraformersmc.terraform.sign.block.TerraformWallSignBlock;
import io.github.sweetberrycollective.wwizardry.WanderingMod;
import net.minecraft.block.*;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.Direction;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;

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
		final var settings = QuiltBlockSettings.of(Material.WOOD).mapColor(wood);
		STRIPPED_WOOD = WanderingBlocks.registerBlock("stripped_"+baseName+"_wood", createLogBlock(wood, wood));
		PLANKS = WanderingBlocks.registerBlock(baseName+"_planks", new Block(settings));
		STAIRS = WanderingBlocks.registerBlock(baseName+"_stairs", new StairsBlock(PLANKS.getDefaultState(), settings));
		SLAB = WanderingBlocks.registerBlock(baseName+"_slab", new SlabBlock(settings));
		BUTTON = WanderingBlocks.registerBlock(baseName+"_button", new WoodenButtonBlock(settings));
		PRESSURE_PLATE = WanderingBlocks.registerBlock(baseName+"_pressure_plate", new PressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, settings));
		DOOR = WanderingBlocks.registerBlock(baseName+"_door", new DoorBlock(settings));
		TRAPDOOR = WanderingBlocks.registerBlock(baseName+"_trapdoor", new TrapdoorBlock(settings));
		SIGN = WanderingBlocks.registerBlock(baseName+"_sign", new TerraformSignBlock(WanderingMod.id("entity/sign/"+baseName), settings));
		SIGN_WALL = WanderingBlocks.registerBlock(baseName+"_wall_sign", new TerraformWallSignBlock(WanderingMod.id("entity/sign/"+baseName), settings));
		FENCE = WanderingBlocks.registerBlock(baseName+"_slab_fence", new FenceBlock(settings));
		FENCE_GATE = WanderingBlocks.registerBlock(baseName+"_slab_fence_gate", new FenceGateBlock(settings));
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
