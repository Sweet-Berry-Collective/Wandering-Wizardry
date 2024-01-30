package dev.sweetberry.wwizardry.content.datagen;

import dev.sweetberry.wwizardry.Mod;
import dev.sweetberry.wwizardry.api.resource.MapBackedPack;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.registry.Holder;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class AbstractDataGenerator {
	public AbstractDataGenerator() {
	}

	public static void reloadPack(ResourceManager manager) {
		DatagenInitializer.pack.clear(ResourceType.CLIENT_RESOURCES);
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

		private Identifier path(String file) {
			return Mod.id("datagen/"+inputPath+"/"+file+".json");
		}

		public void put(MapBackedPack pack, String path, String text) {
			var id = Mod.id(outputPath+"/"+path+".json");
			if (manager.getResource(id).isPresent()) return;

			pack.put(ResourceType.CLIENT_RESOURCES, Mod.id(outputPath+"/"+path+".json"), text.replaceAll("%", baseName));
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
