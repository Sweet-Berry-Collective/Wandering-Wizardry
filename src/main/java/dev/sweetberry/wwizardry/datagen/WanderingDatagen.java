package dev.sweetberry.wwizardry.datagen;

import com.mojang.serialization.Lifecycle;
import dev.sweetberry.wwizardry.WanderingMod;
import dev.sweetberry.wwizardry.block.WanderingBlocks;
import net.minecraft.block.MapColor;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.resource.loader.api.InMemoryPack;

public class WanderingDatagen {
	public static final Identifier REGISTRY_ID = WanderingMod.id("data_generators");
	public static final RegistryKey<Registry<AbstractDataGenerator>> REGISTRY_KEY = RegistryKey.ofRegistry(REGISTRY_ID);
	public static final Registry<AbstractDataGenerator> REGISTRY = new SimpleRegistry<>(REGISTRY_KEY, Lifecycle.stable(), false);

	public static final InMemoryPack pack = new InMemoryPack.Named("Wandering Wizardry resources");

	public static final WoodType DENIA_WOOD = registerDataGenerator("denia_wood", new WoodType("denia", MapColor.ICE, MapColor.DEEPSLATE, BlockSoundGroup.WOOD));
	public static final WoodType MYCHA_WOOD = registerDataGenerator("mycha_wood", new WoodType("mycha", MapColor.PURPLE, MapColor.BLUE, BlockSoundGroup.NETHER_WOOD, WanderingBlocks.MYCELIAL_SAND));

	public static final BrickType CHISELED_BASALT = registerDataGenerator("chiseled_basalt", new BrickType("chiseled_basalt", false, MapColor.GRAY, BlockSoundGroup.BASALT));
	public static final BrickType BASALT_BRICKS = registerDataGenerator("basalt_bricks", new BrickType("basalt_brick", true, MapColor.GRAY, BlockSoundGroup.BASALT));
	public static final BrickType CUT_BASALT = registerDataGenerator("cut_basalt", new BrickType("cut_basalt", false, MapColor.GRAY, BlockSoundGroup.BASALT));
	public static final BrickType BASALT_TILES = registerDataGenerator("basalt_tiles", new BrickType("basalt_tile", true, MapColor.GRAY, BlockSoundGroup.BASALT));

	public static final BrickType MOSSY_CHISELED_BASALT = registerDataGenerator("mossy_chiseled_basalt", new BrickType("mossy_chiseled_basalt", false, MapColor.GRAY, BlockSoundGroup.BASALT));
	public static final BrickType MOSSY_BASALT_BRICKS = registerDataGenerator("mossy_basalt_bricks", new BrickType("mossy_basalt_brick", true, MapColor.GRAY, BlockSoundGroup.BASALT));
	public static final BrickType MOSSY_CUT_BASALT = registerDataGenerator("mossy_cut_basalt", new BrickType("mossy_cut_basalt", false, MapColor.GRAY, BlockSoundGroup.BASALT));
	public static final BrickType MOSSY_BASALT_TILES = registerDataGenerator("mossy_basalt_tiles", new BrickType("mossy_basalt_tile", true, MapColor.GRAY, BlockSoundGroup.BASALT));

	public static void init() {
	}

	public static <T extends AbstractDataGenerator> T registerDataGenerator(String path, T t) {
		return Registry.register(REGISTRY, WanderingMod.id(path), t);
	}
}
