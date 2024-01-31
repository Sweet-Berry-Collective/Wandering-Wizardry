package dev.sweetberry.wwizardry.content.world.processors;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;

public class WaterLoggingFixProcessor extends StructureProcessor {
	public static final Codec<WaterLoggingFixProcessor> CODEC = Codec.unit(WaterLoggingFixProcessor::new);
	public static final StructureProcessorType<WaterLoggingFixProcessor> INSTANCE = () -> CODEC;

	@Nullable
	@Override
	public StructureTemplate.StructureBlockInfo processBlock(LevelReader world, BlockPos pos, BlockPos pivot, StructureTemplate.StructureBlockInfo localBlockInfo, StructureTemplate.StructureBlockInfo absoluteBlockInfo, StructurePlaceSettings placementData) {
		if (localBlockInfo.state().getFluidState().isEmpty())
			return absoluteBlockInfo;
		if (world instanceof WorldGenRegion region && region.getCenter().equals(new ChunkPos(localBlockInfo.pos())))
			return absoluteBlockInfo;
		var chunk = world.getChunk(absoluteBlockInfo.pos());
		var minY = chunk.getMinBuildHeight();
		var maxY = chunk.getMaxBuildHeight();
		var currentY = absoluteBlockInfo.pos().getY();
		if (currentY < minY || currentY > maxY)
			return absoluteBlockInfo;

		((LevelAccessor)world).scheduleTick(absoluteBlockInfo.pos(), absoluteBlockInfo.state().getBlock(), 0);

		return absoluteBlockInfo;
	}

	@Override
	protected StructureProcessorType<?> getType() {
		return INSTANCE;
	}
}
