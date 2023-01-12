package io.github.sweetberrycollective.wwizardry.datagen;

import io.github.sweetberrycollective.wwizardry.WanderingMod;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.quiltmc.qsl.resource.loader.api.InMemoryResourcePack;
import org.quiltmc.qsl.resource.loader.api.ResourcePackRegistrationContext;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class WanderingDatagen implements ResourcePackRegistrationContext.Callback {

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
				WanderingMod.LOGGER.error("Unable to find "+name);
				return "{}";
			}
		}

		private Identifier path(String file) {
			return WanderingMod.id("datagen/"+inputPath+"/"+file+".json");
		}

		public void put(InMemoryResourcePack pack, String path, String text) {
			WanderingMod.LOGGER.info(WanderingMod.id(outputPath+"/"+path+".json").toString());
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
}
