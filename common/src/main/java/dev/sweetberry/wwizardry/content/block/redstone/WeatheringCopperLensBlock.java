package dev.sweetberry.wwizardry.content.block.redstone;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class WeatheringCopperLensBlock extends CopperLensBlock implements WeatheringCopper {
	public final WeatheringCopper.WeatherState weatherState;

	public WeatheringCopperLensBlock(WeatheringCopper.WeatherState state, Properties properties) {
		super(properties);
		this.weatherState = state;
	}

	@Override
	public @NotNull WeatherState getAge() {
		return weatherState;
	}

	protected void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource rand) {
		this.changeOverTime(state, level, pos, rand);
	}

	protected boolean isRandomlyTicking(BlockState state) {
		return WeatheringCopper.getNext(state.getBlock()).isPresent();
	}
}
