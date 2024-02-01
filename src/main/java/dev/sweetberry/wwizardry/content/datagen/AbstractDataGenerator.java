package dev.sweetberry.wwizardry.content.datagen;

import dev.sweetberry.wwizardry.Mod;
import dev.sweetberry.wwizardry.api.resource.MapBackedPack;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class AbstractDataGenerator {
	public AbstractDataGenerator() {
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
			DatagenInitializer.pack.put("pack.png", manager.getResource(Mod.id("icon.png")).get().open().readAllBytes());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		DatagenInitializer.REGISTRY
			.holders()
			.map(Holder.Reference::value)
			.forEach(generator -> generator.onRegisterPack(manager));
	}

	public abstract void onRegisterPack(@NotNull ResourceManager manager);

	public static abstract class AbstractDataApplier {
		public final @NotNull ResourceManager manager;
		public final String baseName;
		public final String inputPath;
		public final String outputPath;

		public AbstractDataApplier(@NotNull ResourceManager manager, String baseName, String inputPath, String outputPath) {
			this.manager = manager;
			this.baseName = baseName;
			this.inputPath = inputPath;
			this.outputPath = outputPath;
		}

		abstract void addToResourcePack(MapBackedPack pack);

		public String getResource(String name) {
			try {
				return new String(manager.open(path(name)).readAllBytes(), StandardCharsets.UTF_8);
			} catch (IOException e) {
				Mod.LOGGER.error("Unable to find "+path(name));
				return "{}";
			}
		}

		private ResourceLocation path(String file) {
			return Mod.id("datagen/"+inputPath+"/"+file+".json");
		}

		public void put(MapBackedPack pack, String path, String text) {
			var id = Mod.id(outputPath+"/"+path+".json");
			if (manager.getResource(id).isPresent()) return;

			pack.put(PackType.CLIENT_RESOURCES, Mod.id(outputPath+"/"+path+".json"), text.replaceAll("%", baseName));
		}

		public void put(MapBackedPack pack, String path, String text, String type) {
			if (type == null) {
				put(pack, path, text.replaceAll("\\$", ""));
			} else {
				put(pack, path+"_"+type, text.replaceAll("\\$", "_"+type));
			}
		}
	}

	public abstract static class AbstractBlockstateDataApplier extends AbstractDataApplier {
		public AbstractBlockstateDataApplier(@NotNull ResourceManager manager, String baseName, String subPath) {
			super(manager, baseName, "blockstates/"+subPath, "blockstates");
		}
	}

	public abstract static class AbstractBlockModelDataApplier extends AbstractDataApplier {
		public AbstractBlockModelDataApplier(@NotNull ResourceManager manager, String baseName, String subPath) {
			super(manager, baseName, "models/block/"+subPath, "models/block");
		}
	}

	public abstract static class AbstractItemModelDataApplier extends AbstractDataApplier {
		public AbstractItemModelDataApplier(@NotNull ResourceManager manager, String baseName, String subPath) {
			super(manager, baseName, "models/item/"+subPath, "models/item");
		}
	}
}
