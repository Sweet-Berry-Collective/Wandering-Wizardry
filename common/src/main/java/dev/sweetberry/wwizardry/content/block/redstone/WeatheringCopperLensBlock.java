package dev.sweetberry.wwizardry.content.block.redstone;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class WeatheringCopperLensBlock extends CopperLensBlock implements WeatheringCopper {
	public WeatheringCopperLensBlock(@NotNull WeatheringCopper.WeatherState state, Properties properties) {
		super(state, properties);
	}

	@Override
	public @NotNull WeatheringCopper.WeatherState getAge() {
		return weatherState;
	}

	@Override
	protected void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource rand) {
		this.changeOverTime(state, level, pos, rand);
	}

	@Override
	protected boolean isRandomlyTicking(BlockState state) {
		return WeatheringCopper.getNext(state.getBlock()).isPresent();
	}
}
