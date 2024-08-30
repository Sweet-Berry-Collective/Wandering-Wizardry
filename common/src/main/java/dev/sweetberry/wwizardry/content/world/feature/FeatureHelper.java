package dev.sweetberry.wwizardry.content.world.feature;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import org.jetbrains.annotations.NotNull;

public class FeatureHelper {
	// Fast voxel traversal algorithm
	public static <FC extends FeatureConfiguration> void drawLine(@NotNull FeaturePlaceContext<FC> context, BlockPos origin, BlockPos dest, BlockStateProvider provider) {
		var level = context.level();
		var rand = context.random();

		float
			originX = origin.getX(),
			originY = origin.getY(),
			originZ = origin.getZ(),
			destX = dest.getX(),
			destY = dest.getY(),
			destZ = dest.getZ(),
			deltaX = destX - originX,
			deltaY = destY - originY,
			deltaZ = destZ - originZ,
			stepX = Math.signum(deltaX),
			stepY = Math.signum(deltaY),
			stepZ = Math.signum(deltaZ),
			tDeltaX = 1 / Math.abs(deltaX),
			tDeltaY = 1 / Math.abs(deltaY),
			tDeltaZ = 1 / Math.abs(deltaZ),
			tMaxX = getTMax(originX, tDeltaX, stepX),
			tMaxY = getTMax(originY, tDeltaY, stepY),
			tMaxZ = getTMax(originZ, tDeltaZ, stepZ),
			x = originX,
			y = originY,
			z = originZ,
			maxDist = (float)Math.sqrt((deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ));

		final int MAX_ITER = 1024;

		for (int i = 0; i < MAX_ITER; i++) {
			float
				traversedX = x - originX,
				traversedY = y - originY,
				traversedZ = z - originZ,
				dist = (float)Math.sqrt((traversedX * traversedX) + (traversedY * traversedY) + (traversedZ * traversedZ));

			var blockPos = new BlockPos((int)Math.floor(x), (int)Math.floor(y), (int)Math.floor(z));

			if (level.ensureCanWrite(blockPos) && level.getBlockState(blockPos).isAir())
				level.setBlock(blockPos, provider.getState(rand, blockPos), Block.UPDATE_ALL);

			if (blockPos.equals(dest) || dist > maxDist)
				break;

			if (tMaxX < tMaxY && tMaxX < tMaxZ) {
				x += stepX;
				tMaxX += tDeltaX;
			} else if (tMaxY < tMaxZ) {
				y += stepY;
				tMaxY += tDeltaY;
			} else {
				z += stepZ;
				tMaxZ += tDeltaZ;
			}
		}
	}

	private static float mod1(float value) {
		return (value % 1 + 1) % 1;
	}
	private static float fixNaN(float value) {
		return Float.isNaN(value) ? 0 : value;
	}
	private static float getTMax(float start, float tDelta, float step) {
		return fixNaN(tDelta * (step > 0 ? 1 - mod1(start) : mod1(start)));
	}
}
