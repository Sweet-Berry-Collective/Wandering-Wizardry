package dev.sweetberry.wwizardry.content.world.processors;

import com.mojang.serialization.Codec;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class WaterLoggingFixProcessor extends StructureProcessor {
	public static final Codec<WaterLoggingFixProcessor> CODEC = Codec.unit(WaterLoggingFixProcessor::new);
	public static final StructureProcessorType<WaterLoggingFixProcessor> INSTANCE = () -> CODEC;

	@Nullable
	@Override
	public Structure.StructureBlockInfo process(WorldView world, BlockPos pos, BlockPos pivot, Structure.StructureBlockInfo localBlockInfo, Structure.StructureBlockInfo absoluteBlockInfo, StructurePlacementData placementData) {
		if (localBlockInfo.state().getFluidState().isEmpty())
			return absoluteBlockInfo;
		if (world instanceof ChunkRegion region && region.getCenterPos().equals(new ChunkPos(localBlockInfo.pos())))
			return absoluteBlockInfo;
		var chunk = world.getChunk(absoluteBlockInfo.pos());
		var minY = chunk.getBottomY();
		var maxY = chunk.getTopY();
		var currentY = absoluteBlockInfo.pos().getY();
		if (currentY < minY || currentY > maxY)
			return absoluteBlockInfo;

		((WorldAccess)world).scheduleBlockTick(absoluteBlockInfo.pos(), absoluteBlockInfo.state().getBlock(), 0);

		return absoluteBlockInfo;
	}

	@Override
	protected StructureProcessorType<?> getType() {
		return INSTANCE;
	}
}
