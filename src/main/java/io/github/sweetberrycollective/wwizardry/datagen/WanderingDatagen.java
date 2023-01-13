package io.github.sweetberrycollective.wwizardry.datagen;

import com.mojang.serialization.Lifecycle;
import io.github.sweetberrycollective.wwizardry.WanderingMod;
import net.minecraft.block.MapColor;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import org.quiltmc.qsl.resource.loader.api.InMemoryResourcePack;

public class WanderingDatagen {
	public static final Identifier REGISTRY_ID = WanderingMod.id("data_generators");
	public static final RegistryKey<Registry<AbstractDataGenerator>> REGISTRY_KEY = RegistryKey.ofRegistry(REGISTRY_ID);
	public static final Registry<AbstractDataGenerator> REGISTRY = new SimpleRegistry<>(REGISTRY_KEY, Lifecycle.stable(), null);

	public static final InMemoryResourcePack pack = new InMemoryResourcePack.Named("Wandering Wizardry resources");

	public static final WoodType DENIA_WOOD = registerDataGenerator("denia_wood", new WoodType("denia", MapColor.DARK_AQUA, MapColor.DEEPSLATE_GRAY, BlockSoundGroup.WOOD));
	public static final BrickType CHISELED_BASALT = registerDataGenerator("chiseled_basalt", new BrickType("chiseled_basalt", false, MapColor.GRAY, BlockSoundGroup.BASALT));
	public static final BrickType BASALT_BRICKS = registerDataGenerator("basalt_bricks", new BrickType("basalt_brick", true, MapColor.GRAY, BlockSoundGroup.BASALT));
	public static final BrickType BASALT_TILES = registerDataGenerator("basalt_tiles", new BrickType("basalt_tile", true, MapColor.GRAY, BlockSoundGroup.BASALT));

	public static void init() {
	}

	public static <T extends AbstractDataGenerator> T registerDataGenerator(String path, T t) {
		return Registry.register(REGISTRY, WanderingMod.id(path), t);
	}
}
