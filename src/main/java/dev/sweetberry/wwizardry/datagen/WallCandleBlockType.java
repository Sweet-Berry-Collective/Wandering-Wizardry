package dev.sweetberry.wwizardry.datagen;

import dev.sweetberry.wwizardry.block.WanderingBlocks;
import dev.sweetberry.wwizardry.block.WallCandleBlock;
import dev.sweetberry.wwizardry.block.WallHolderBlock;
import net.minecraft.block.CandleBlock;
import net.minecraft.resource.MultiPackResourceManager;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;
import org.quiltmc.qsl.resource.loader.api.InMemoryResourcePack;
import org.quiltmc.qsl.resource.loader.api.ResourcePackRegistrationContext;

public class WallCandleBlockType extends AbstractDataGenerator {
	public final Identifier id;
	public final CandleBlock candleBlock;
	public final WallCandleBlock wallCandleBlock;

	public WallCandleBlockType(Identifier id, CandleBlock candleBlock) {
		this.id = id;
		this.candleBlock = candleBlock;
		wallCandleBlock = new WallCandleBlock(QuiltBlockSettings.copyOf(WallHolderBlock.EMPTY), candleBlock);

		WanderingBlocks.registerBlock(transformId(id), wallCandleBlock);
	}

	public static String transformId(Identifier id) {
		if (id.getNamespace().equals("minecraft"))
			return "wall_holder/"+id.getPath();
		return "wall_holder/"+id.getNamespace()+"/"+id.getPath();
	}

	@Override
	public void onRegisterPack(@NotNull ResourcePackRegistrationContext context) {
		var manager = context.resourceManager();
		if (!(manager instanceof MultiPackResourceManager multiManager)) return;
		var pack = WanderingDatagen.pack;
		var blockstates = new BlockstateDataApplier(context, transformId(id));
		var blockModels = new BlockModelDataApplier(context, transformId(id), this);
		blockstates.addToResourcePack(pack);
		blockModels.addToResourcePack(pack);
	}

	public String getExpectedTexturePath() {
		return id.getNamespace()+":block/"+id.getPath();
	}

	public String getExpectedTexturePathLit() {
		return getExpectedTexturePath()+"_lit";
	}

	public static class BlockstateDataApplier extends AbstractDataGenerator.AbstractBlockstateDataApplier {
		public final String blockState;

		public BlockstateDataApplier(@NotNull ResourcePackRegistrationContext context, String baseName) {
			super(context, baseName, "wall_holder");

			blockState = getResource("bistate");
		}

		@Override
		void addToResourcePack(InMemoryResourcePack pack) {
			put(pack, baseName, blockState);
		}
	}

	public static class BlockModelDataApplier extends AbstractDataGenerator.AbstractBlockModelDataApplier {
		public final String model;
		public final WallCandleBlockType type;

		public BlockModelDataApplier(@NotNull ResourcePackRegistrationContext context, String baseName, WallCandleBlockType type) {
			super(context, baseName, "wall_holder");

			model = getResource("candle");
			this.type = type;
		}

		@Override
		void addToResourcePack(InMemoryResourcePack pack) {
			put(pack, baseName, model.replaceAll("#", type.getExpectedTexturePath()));
			put(pack, baseName+"_lit", model.replaceAll("#", type.getExpectedTexturePathLit()));
		}
	}
}
