package dev.sweetberry.wwizardry.content.datagen;

import com.mojang.serialization.Lifecycle;
import dev.sweetberry.wwizardry.Mod;
import dev.sweetberry.wwizardry.api.resource.MapBackedPack;
import dev.sweetberry.wwizardry.content.block.BlockInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.MapColor;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.resource.ResourceType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.resource.loader.api.PackRegistrationContext;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;
import org.quiltmc.qsl.resource.loader.api.ResourceLoaderEvents;

public class DatagenInitializer {
	public static final Identifier REGISTRY_ID = Mod.id("data_generators");
	public static final RegistryKey<Registry<AbstractDataGenerator>> REGISTRY_KEY = RegistryKey.ofRegistry(REGISTRY_ID);
	public static final Registry<AbstractDataGenerator> REGISTRY = new SimpleRegistry<>(REGISTRY_KEY, Lifecycle.stable(), false);

	public static final MapBackedPack pack = new MapBackedPack();

	public static final WoodType DENIA_WOOD = registerDataGenerator("denia_wood", new WoodType("denia", MapColor.ICE, MapColor.DEEPSLATE, BlockSoundGroup.WOOD));
	public static final WoodType MYCHA_WOOD = registerDataGenerator("mycha_wood", new WoodType("mycha", MapColor.PURPLE, MapColor.BLUE, BlockSoundGroup.NETHER_WOOD, BlockInitializer.MYCELIAL_SAND));

	public static final BrickType CHISELED_BASALT = registerDataGenerator("chiseled_basalt", new BrickType("chiseled_basalt", false, MapColor.GRAY, BlockSoundGroup.BASALT));
	public static final BrickType BASALT_BRICKS = registerDataGenerator("basalt_bricks", new BrickType("basalt_brick", true, MapColor.GRAY, BlockSoundGroup.BASALT));
	public static final BrickType CUT_BASALT = registerDataGenerator("cut_basalt", new BrickType("cut_basalt", false, MapColor.GRAY, BlockSoundGroup.BASALT));
	public static final BrickType BASALT_TILES = registerDataGenerator("basalt_tiles", new BrickType("basalt_tile", true, MapColor.GRAY, BlockSoundGroup.BASALT));

	public static final BrickType MOSSY_CHISELED_BASALT = registerDataGenerator("mossy_chiseled_basalt", new BrickType("mossy_chiseled_basalt", false, MapColor.GRAY, BlockSoundGroup.BASALT));
	public static final BrickType MOSSY_BASALT_BRICKS = registerDataGenerator("mossy_basalt_bricks", new BrickType("mossy_basalt_brick", true, MapColor.GRAY, BlockSoundGroup.BASALT));
	public static final BrickType MOSSY_CUT_BASALT = registerDataGenerator("mossy_cut_basalt", new BrickType("mossy_cut_basalt", false, MapColor.GRAY, BlockSoundGroup.BASALT));
	public static final BrickType MOSSY_BASALT_TILES = registerDataGenerator("mossy_basalt_tiles", new BrickType("mossy_basalt_tile", true, MapColor.GRAY, BlockSoundGroup.BASALT));

	public static void init() {
		if (FabricLoader.getInstance().isModLoaded("quilt_resource_loader"))
			initWithQsl();
	}

	public static <T extends AbstractDataGenerator> T registerDataGenerator(String path, T t) {
		return Registry.register(REGISTRY, Mod.id(path), t);
	}

	public static void initWithQsl() {
		ResourceLoader.get(ResourceType.CLIENT_RESOURCES).getRegisterDefaultPackEvent().register(ctx -> {
			AbstractDataGenerator.reloadPack(ctx.resourceManager());
			ctx.addResourcePack(pack);
		});
	}
}
