package dev.sweetberry.wwizardry.content.world.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.content.world.WorldgenInitializer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.GeodeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class CrystalShardFeature extends Feature<CrystalShardFeature.Config> {
	public CrystalShardFeature(Codec<Config> codec) {
		super(codec);
	}

	@Override
	public boolean place(@NotNull FeaturePlaceContext<Config> context) {
		var origin = context.origin();
		var rand = context.random();
		var conf = context.config();
		var level = context.level();

		var block = level.getBlockState(origin);
		var check = FeatureHelper.canReplace(block);
		if (conf.ceiling) {
			while (FeatureHelper.canReplace(block) == check) {
				origin = check ? origin.above() : origin.below();
				if (origin.getY() > level.getMaxBuildHeight() || origin.getY() < level.getMinBuildHeight())
					return false;
				block = level.getBlockState(origin);
			}
		} else {
			while (FeatureHelper.canReplace(block) == check) {
				origin = check ? origin.below() : origin.above();
				if (origin.getY() > level.getMaxBuildHeight() || origin.getY() < level.getMinBuildHeight())
					return false;
				block = level.getBlockState(origin);
			}
		}
		if (!level.getBiome(origin).is(WorldgenInitializer.CRYSTAL_COVE))
			return false;

		// Get a random rotation
		final var tau = (float) Math.TAU;
		final var halfPi = (float) Math.PI / 2;
		var q = new Quaternionf()
			.rotateLocalX(rand.nextFloat() * halfPi + (conf.ceiling ? halfPi : 0))
			.rotateLocalY(rand.nextFloat() * tau);

		var length = context.config().length.sample(rand);
		var projected = new Vector3f(
			0,
			length,
			0
		).rotate(q);
		var dest = origin.offset((int)projected.x, (int)projected.y, (int)projected.z);

		// Draw the lines
		drawLines(context, origin, dest, conf.radius.sample(rand), conf.state);

		return true;
	}

	private void drawLines(@NotNull FeaturePlaceContext<Config> context, BlockPos origin, BlockPos dest, float radius, BlockStateProvider state) {
		final int intRadius = (int) Math.ceil(radius);

		for (int x = -intRadius; x <= intRadius; x++) {
			for (int y = -intRadius; y <= intRadius; y++) {
				for (int z = -intRadius; z <= intRadius; z++) {
					float dist = (float)Math.sqrt((x * x) + (y * y) + (z * z));
					if (dist > radius)
						continue;
					var pos = origin.offset(x, y, z);
					FeatureHelper.drawLine(context, pos, dest, state, this::safeSetBlock);
				}
			}
		}
	}

	public record Config(
		BlockStateProvider state,
		FloatProvider radius,
		FloatProvider length,
		boolean ceiling
	) implements FeatureConfiguration {
		public static final Codec<Config> CODEC = RecordCodecBuilder.create(inst -> inst.group(
			BlockStateProvider.CODEC.fieldOf("state").forGetter(Config::state),
			FloatProvider.CODEC.fieldOf("radius").forGetter(Config::radius),
			FloatProvider.CODEC.fieldOf("length").forGetter(Config::length),
			Codec.BOOL.optionalFieldOf("ceiling", false).forGetter(Config::ceiling)
		).apply(inst, Config::new));
	}
}
