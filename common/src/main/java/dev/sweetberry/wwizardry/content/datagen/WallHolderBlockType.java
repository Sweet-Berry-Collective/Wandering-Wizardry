package dev.sweetberry.wwizardry.content.datagen;

import dev.sweetberry.wwizardry.api.resource.MapBackedPack;
import dev.sweetberry.wwizardry.content.block.BlockInitializer;
import dev.sweetberry.wwizardry.content.block.WallCandleBlock;
import dev.sweetberry.wwizardry.content.block.WallHolderBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;

public class WallHolderBlockType extends AbstractDataGenerator {
	public final ResourceLocation id;
	public final Block block;
	public final WallHolderBlock wallBlock;
	public final ParentType parent;

	public WallHolderBlockType(ResourceLocation id, Block block, ParentType parent) {
		this.id = id;
		this.block = block;
		this.parent = parent;
		wallBlock = switch (parent) {
			case CANDLE -> new WallCandleBlock(BlockBehaviour.Properties.ofFullCopy(WallHolderBlock.EMPTY), (CandleBlock) block);
			// TODO!
			default -> throw new NotImplementedException("Type "+ parent.name +" is not implemented.");
		};

		BlockInitializer.registerBlock(transformId(id), wallBlock);
	}

	public static String transformId(ResourceLocation id) {
		if (id.getNamespace().equals("minecraft"))
			return "wall_holder/"+id.getPath();
		return "wall_holder/"+id.getNamespace()+"/"+id.getPath();
	}

	@Override
	public void onRegisterPack(@NotNull ResourceManager manager, MapBackedPack pack) {
		var blockstates = new BlockstateDataApplier(manager, transformId(id), this);
		var blockModels = new BlockModelDataApplier(manager, transformId(id), this);
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

		public BlockstateDataApplier(@NotNull ResourceManager manager, String baseName, WallHolderBlockType type) {
			super(manager, baseName, "wall_holder");

			blockState = getResource(type.parent.isToggleable ? "bistate" : "monostate");
		}

		@Override
		void addToResourcePack(MapBackedPack pack) {
			put(pack, baseName, blockState);
		}
	}

	public static class BlockModelDataApplier extends AbstractDataGenerator.AbstractBlockModelDataApplier {
		public final String model;
		public final WallHolderBlockType type;

		public BlockModelDataApplier(@NotNull ResourceManager manager, String baseName, WallHolderBlockType type) {
			super(manager, baseName, "wall_holder");

			model = getResource(type.parent.name);
			this.type = type;
		}

		@Override
		void addToResourcePack(MapBackedPack pack) {
			if (type.parent.isToggleable) {
				put(pack, baseName, model.replaceAll("#", type.getExpectedTexturePath(type.parent)));
				put(pack, baseName+"_lit", model.replaceAll("#", type.getExpectedTexturePathLit(type.parent)));
			} else {
				put(pack, baseName, model.replaceAll("#", type.getExpectedTexturePath(type.parent)));
			}
		}
	}
}
