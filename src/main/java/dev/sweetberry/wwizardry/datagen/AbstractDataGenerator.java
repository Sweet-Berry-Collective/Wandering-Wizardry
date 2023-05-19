package dev.sweetberry.wwizardry.datagen;

import dev.sweetberry.wwizardry.WanderingMod;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.quiltmc.qsl.resource.loader.api.InMemoryResourcePack;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;
import org.quiltmc.qsl.resource.loader.api.ResourcePackRegistrationContext;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class AbstractDataGenerator {
	static {
		ResourceLoader.get(ResourceType.CLIENT_RESOURCES).getRegisterDefaultResourcePackEvent().register(context -> {
			WanderingDatagen.pack.clearResources();
			WanderingDatagen.REGISTRY.forEach(generator -> generator.onRegisterPack(context));
			context.addResourcePack(WanderingDatagen.pack);
		});
	}

	public AbstractDataGenerator() {
	}

	public abstract void onRegisterPack(@NotNull ResourcePackRegistrationContext context);

	public static abstract class AbstractDataApplier {
		public final @NotNull ResourcePackRegistrationContext context;
		public final String baseName;
		public final String inputPath;
		public final String outputPath;

		public AbstractDataApplier(@NotNull ResourcePackRegistrationContext context, String baseName, String inputPath, String outputPath) {
			this.context = context;
			this.baseName = baseName;
			this.inputPath = inputPath;
			this.outputPath = outputPath;
		}

		abstract void addToResourcePack(InMemoryResourcePack pack);

		public String getResource(String name) {
			try {
				return new String(context.resourceManager().open(path(name)).readAllBytes(), StandardCharsets.UTF_8);
			} catch (IOException e) {
				WanderingMod.LOGGER.error("Unable to find "+path(name));
				return "{}";
			}
		}

		private Identifier path(String file) {
			return WanderingMod.id("datagen/"+inputPath+"/"+file+".json");
		}

		public void put(InMemoryResourcePack pack, String path, String text) {
			var id = WanderingMod.id(outputPath+"/"+path+".json");
			if (context.resourceManager().getResource(id).isPresent()) return;

			pack.putText(ResourceType.CLIENT_RESOURCES, WanderingMod.id(outputPath+"/"+path+".json"), text.replaceAll("%", baseName));
		}

		public void put(InMemoryResourcePack pack, String path, String text, String type) {
			if (type == null) {
				put(pack, path, text.replaceAll("\\$", ""));
			} else {
				put(pack, path+"_"+type, text.replaceAll("\\$", "_"+type));
			}
		}
	}

	public abstract static class AbstractBlockstateDataApplier extends AbstractDataApplier {
		public AbstractBlockstateDataApplier(@NotNull ResourcePackRegistrationContext context, String baseName, String subPath) {
			super(context, baseName, "blockstates/"+subPath, "blockstates");
		}
	}

	public abstract static class AbstractBlockModelDataApplier extends AbstractDataApplier {
		public AbstractBlockModelDataApplier(@NotNull ResourcePackRegistrationContext context, String baseName, String subPath) {
			super(context, baseName, "models/block/"+subPath, "models/block");
		}
	}

	public abstract static class AbstractItemModelDataApplier extends AbstractDataApplier {
		public AbstractItemModelDataApplier(@NotNull ResourcePackRegistrationContext context, String baseName, String subPath) {
			super(context, baseName, "models/item/"+subPath, "models/item");
		}
	}
}
