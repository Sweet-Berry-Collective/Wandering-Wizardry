package dev.sweetberry.wwizardry.content.datagen;

import com.mojang.serialization.Lifecycle;
import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.api.resource.MapBackedPack;
import dev.sweetberry.wwizardry.content.block.BlockInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;

public class DatagenInitializer {
	public static final ResourceLocation REGISTRY_ID = WanderingWizardry.id("data_generators");
	public static final ResourceKey<Registry<AbstractDataGenerator>> REGISTRY_KEY = ResourceKey.createRegistryKey(REGISTRY_ID);
	public static final Registry<AbstractDataGenerator> REGISTRY = new MappedRegistry<>(REGISTRY_KEY, Lifecycle.stable(), false);

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

	public static void init() {
		if (FabricLoader.getInstance().isModLoaded("quilt_resource_loader"))
			initWithQsl();
	}

	public static <T extends AbstractDataGenerator> T registerDataGenerator(String path, T t) {
		return Registry.register(REGISTRY, WanderingWizardry.id(path), t);
	}

	public static void initWithQsl() {
		ResourceLoader.get(PackType.CLIENT_RESOURCES).getRegisterDefaultPackEvent().register(ctx -> {
			AbstractDataGenerator.reloadPack(ctx.resourceManager());
			ctx.addResourcePack(pack);
		});
	}
}
