package dev.sweetberry.wwizardry.content.datagen;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.api.resource.MapBackedPack;
import dev.sweetberry.wwizardry.content.block.BlockInitializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DatagenInitializer {
	public static final Map<ResourceLocation, AbstractDataGenerator> REGISTRY = new HashMap<>();

	public static final MapBackedPack pack = new MapBackedPack();

	public static final WoodType DENIA_WOOD = registerDataGenerator("denia_wood", new WoodType("denia", MapColor.ICE, MapColor.DEEPSLATE, SoundType.WOOD));
	public static final WoodType MYCHA_WOOD = registerDataGenerator("mycha_wood", new WoodType("mycha", MapColor.COLOR_PURPLE, MapColor.COLOR_BLUE, SoundType.NETHER_WOOD, BlockInitializer.MYCELIAL_SAND));

	public static final BrickType CHISELED_BASALT = registerDataGenerator("chiseled_basalt", new BrickType("chiseled_basalt", false, MapColor.COLOR_GRAY, SoundType.BASALT));
	public static final BrickType BASALT_BRICKS = registerDataGenerator("basalt_bricks", new BrickType("basalt_brick", true, MapColor.COLOR_GRAY, SoundType.BASALT));
	public static final BrickType CUT_BASALT = registerDataGenerator("cut_basalt", new BrickType("cut_basalt", false, MapColor.COLOR_GRAY, SoundType.BASALT));
	public static final BrickType BASALT_TILES = registerDataGenerator("basalt_tiles", new BrickType("basalt_tile", true, MapColor.COLOR_GRAY, SoundType.BASALT));

	public static final BrickType MOSSY_CHISELED_BASALT = registerDataGenerator("mossy_chiseled_basalt", new BrickType("mossy_chiseled_basalt", false, MapColor.COLOR_GRAY, SoundType.BASALT));
	public static final BrickType MOSSY_BASALT_BRICKS = registerDataGenerator("mossy_basalt_bricks", new BrickType("mossy_basalt_brick", true, MapColor.COLOR_GRAY, SoundType.BASALT));
	public static final BrickType MOSSY_CUT_BASALT = registerDataGenerator("mossy_cut_basalt", new BrickType("mossy_cut_basalt", false, MapColor.COLOR_GRAY, SoundType.BASALT));
	public static final BrickType MOSSY_BASALT_TILES = registerDataGenerator("mossy_basalt_tiles", new BrickType("mossy_basalt_tile", true, MapColor.COLOR_GRAY, SoundType.BASALT));

	public static void init() {}

	public static <T extends AbstractDataGenerator> T registerDataGenerator(String path, T t) {
		REGISTRY.put(WanderingWizardry.id(path), t);
		return t;
	}

	public static void reloadPack(ResourceManager manager) {
		DatagenInitializer.pack.clear(PackType.CLIENT_RESOURCES);
		DatagenInitializer.pack.put("pack.mcmeta", """
			{
				"pack": {
					"pack_format": 15,
					"description": "Wandering Wizardry Resources"
				}
			}
			""");
		try {
			DatagenInitializer.pack.put("pack.png", manager.getResource(WanderingWizardry.id("icon.png")).get().open().readAllBytes());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		DatagenInitializer.REGISTRY
			.values()
			.forEach(generator -> generator.onRegisterPack(manager, pack));
	}
}
