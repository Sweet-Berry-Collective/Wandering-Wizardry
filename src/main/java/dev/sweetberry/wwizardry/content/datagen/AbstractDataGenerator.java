package dev.sweetberry.wwizardry.content.datagen;

import dev.sweetberry.wwizardry.Mod;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.quiltmc.qsl.resource.loader.api.InMemoryPack;
import org.quiltmc.qsl.resource.loader.api.PackRegistrationContext;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class AbstractDataGenerator {
	static {
		ResourceLoader.get(ResourceType.CLIENT_RESOURCES).getRegisterDefaultPackEvent().register(context -> {
			DatagenInitializer.pack.clearResources();
			DatagenInitializer.REGISTRY.forEach(generator -> generator.onRegisterPack(context));
			context.addResourcePack(DatagenInitializer.pack);
		});
	}

	public AbstractDataGenerator() {
	}

	public abstract void onRegisterPack(@NotNull PackRegistrationContext context);

	public static abstract class AbstractDataApplier {
		public final @NotNull PackRegistrationContext context;
		public final String baseName;
		public final String inputPath;
		public final String outputPath;

		public AbstractDataApplier(@NotNull PackRegistrationContext context, String baseName, String inputPath, String outputPath) {
			this.context = context;
			this.baseName = baseName;
			this.inputPath = inputPath;
			this.outputPath = outputPath;
		}

		abstract void addToResourcePack(InMemoryPack pack);

		public String getResource(String name) {
			try {
				return new String(context.resourceManager().open(path(name)).readAllBytes(), StandardCharsets.UTF_8);
			} catch (IOException e) {
				Mod.LOGGER.error("Unable to find "+path(name));
				return "{}";
			}
		}

		private Identifier path(String file) {
			return Mod.id("datagen/"+inputPath+"/"+file+".json");
		}

		public void put(InMemoryPack pack, String path, String text) {
			var id = Mod.id(outputPath+"/"+path+".json");
			if (context.resourceManager().getResource(id).isPresent()) return;

			pack.putText(ResourceType.CLIENT_RESOURCES, Mod.id(outputPath+"/"+path+".json"), text.replaceAll("%", baseName));
		}

		public void put(InMemoryPack pack, String path, String text, String type) {
			if (type == null) {
				put(pack, path, text.replaceAll("\\$", ""));
			} else {
				put(pack, path+"_"+type, text.replaceAll("\\$", "_"+type));
			}
		}
	}

	public abstract static class AbstractBlockstateDataApplier extends AbstractDataApplier {
		public AbstractBlockstateDataApplier(@NotNull PackRegistrationContext context, String baseName, String subPath) {
			super(context, baseName, "blockstates/"+subPath, "blockstates");
		}
	}

	public abstract static class AbstractBlockModelDataApplier extends AbstractDataApplier {
		public AbstractBlockModelDataApplier(@NotNull PackRegistrationContext context, String baseName, String subPath) {
			super(context, baseName, "models/block/"+subPath, "models/block");
		}
	}

	public abstract static class AbstractItemModelDataApplier extends AbstractDataApplier {
		public AbstractItemModelDataApplier(@NotNull PackRegistrationContext context, String baseName, String subPath) {
			super(context, baseName, "models/item/"+subPath, "models/item");
		}
	}
}
