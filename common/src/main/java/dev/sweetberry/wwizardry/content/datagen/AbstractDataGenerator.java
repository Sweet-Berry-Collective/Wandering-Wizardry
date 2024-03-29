package dev.sweetberry.wwizardry.content.datagen;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.api.event.Event;
import dev.sweetberry.wwizardry.api.resource.MapBackedPack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public abstract class AbstractDataGenerator {
	public static Event<Consumer<AbstractDataGenerator>> DATA_GENERATOR_CONSTRUCTED = new Event<>(listeners -> (abstractDataGenerator -> {
		for (var listener : listeners)
			listener.accept(abstractDataGenerator);
	}));

	public AbstractDataGenerator() {}

	public abstract void onRegisterPack(@NotNull ResourceManager manager, MapBackedPack pack);

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
				WanderingWizardry.LOGGER.error("Unable to find "+path(name));
				return "{}";
			}
		}

		private ResourceLocation path(String file) {
			return WanderingWizardry.id("datagen/"+inputPath+"/"+file+".json");
		}

		public void put(MapBackedPack pack, String path, String text) {
			var id = WanderingWizardry.id(outputPath+"/"+path+".json");
			if (manager.getResource(id).isPresent()) return;

			pack.put(PackType.CLIENT_RESOURCES, WanderingWizardry.id(outputPath+"/"+path+".json"), text.replaceAll("%", baseName));
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
