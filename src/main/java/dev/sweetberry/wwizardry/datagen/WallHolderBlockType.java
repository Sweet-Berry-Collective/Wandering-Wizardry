package dev.sweetberry.wwizardry.datagen;

import dev.sweetberry.wwizardry.block.WanderingBlocks;
import dev.sweetberry.wwizardry.block.WallCandleBlock;
import dev.sweetberry.wwizardry.block.WallHolderBlock;
import net.minecraft.block.Block;
import net.minecraft.block.CandleBlock;
import net.minecraft.resource.MultiPackResourceManager;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;
import org.quiltmc.qsl.resource.loader.api.InMemoryResourcePack;
import org.quiltmc.qsl.resource.loader.api.ResourcePackRegistrationContext;

public class WallHolderBlockType extends AbstractDataGenerator {
	public final Identifier id;
	public final Block block;
	public final WallHolderBlock wallBlock;
	public final ParentType parent;

	public WallHolderBlockType(Identifier id, Block block, ParentType parent) {
		this.id = id;
		this.block = block;
		this.parent = parent;
		wallBlock = switch (parent) {
			case CANDLE -> new WallCandleBlock(QuiltBlockSettings.copyOf(WallHolderBlock.EMPTY), (CandleBlock) block);
			// TODO!
			default -> throw new NotImplementedException("Type "+ parent.name +" is not implemented.");
		};

		WanderingBlocks.registerBlock(transformId(id), wallBlock);
	}

	public static String transformId(Identifier id) {
		if (id.getNamespace().equals("minecraft"))
			return "wall_holder/"+id.getPath();
		return "wall_holder/"+id.getNamespace()+"/"+id.getPath();
	}

	@Override
	public void onRegisterPack(@NotNull ResourcePackRegistrationContext context) {
		var manager = context.resourceManager();
		if (!(manager instanceof MultiPackResourceManager)) return;
		var pack = WanderingDatagen.pack;
		var blockstates = new BlockstateDataApplier(context, transformId(id), this);
		var blockModels = new BlockModelDataApplier(context, transformId(id), this);
		blockstates.addToResourcePack(pack);
		blockModels.addToResourcePack(pack);
	}

	public enum ParentType {
		CANDLE("candle", true),
		TORCH("torch", false),
		TORCH_TOGGLEABLE("torch", true),
		LANTERN("lantern", false),
		LANTERN_TOGGLEABLE("lantern", true);
		public final String name;
		public final boolean isToggleable;
		ParentType(String name, boolean isToggleable) {
			this.name = name;
			this.isToggleable = isToggleable;
		}
	}

	public String getExpectedTexturePath(ParentType parent) {
		var base = id.getNamespace()+":block/"+id.getPath();
		if (parent == ParentType.CANDLE || !parent.isToggleable)
			return base;
		return base+"_off";
	}

	public String getExpectedTexturePathLit(ParentType parent) {
		var base = id.getNamespace()+":block/"+id.getPath();
		if (parent == ParentType.CANDLE)
			return base+"_lit";
		return base;
	}

	public static class BlockstateDataApplier extends AbstractDataGenerator.AbstractBlockstateDataApplier {
		public final String blockState;

		public BlockstateDataApplier(@NotNull ResourcePackRegistrationContext context, String baseName, WallHolderBlockType type) {
			super(context, baseName, "wall_holder");

			blockState = getResource(type.parent.isToggleable ? "bistate" : "monostate");
		}

		@Override
		void addToResourcePack(InMemoryResourcePack pack) {
			put(pack, baseName, blockState);
		}
	}

	public static class BlockModelDataApplier extends AbstractDataGenerator.AbstractBlockModelDataApplier {
		public final String model;
		public final WallHolderBlockType type;

		public BlockModelDataApplier(@NotNull ResourcePackRegistrationContext context, String baseName, WallHolderBlockType type) {
			super(context, baseName, "wall_holder");

			model = getResource(type.parent.name);
			this.type = type;
		}

		@Override
		void addToResourcePack(InMemoryResourcePack pack) {
			if (type.parent.isToggleable) {
				put(pack, baseName, model.replaceAll("#", type.getExpectedTexturePath(type.parent)));
				put(pack, baseName+"_lit", model.replaceAll("#", type.getExpectedTexturePathLit(type.parent)));
			} else {
				put(pack, baseName, model.replaceAll("#", type.getExpectedTexturePath(type.parent)));
			}
		}
	}
}
