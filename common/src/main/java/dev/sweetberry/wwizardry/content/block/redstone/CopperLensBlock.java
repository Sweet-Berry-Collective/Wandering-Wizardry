package dev.sweetberry.wwizardry.content.block.redstone;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;

public class CopperLensBlock extends Block {
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final EnumProperty<Focus> FOCUS = EnumProperty.create("focus", Focus.class);
	public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;

	public final @NotNull WeatheringCopper.WeatherState weatherState;

	public CopperLensBlock(@NotNull WeatheringCopper.WeatherState state, Properties properties) {
		super(properties);
		this.weatherState = state;
		registerDefaultState(defaultBlockState().setValue(POWERED, false).setValue(FOCUS, Focus.FOCUSED).setValue(AXIS, Direction.Axis.Y));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(POWERED, FOCUS, AXIS);
	}

	@Override
	protected @NotNull BlockState rotate(@NotNull BlockState state, @NotNull Rotation rotation) {
		return RotatedPillarBlock.rotatePillar(state, rotation);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(AXIS, context.getClickedFace().getAxis());
	}

	protected void onPlace(BlockState $$0, Level $$1, BlockPos $$2, BlockState $$3, boolean $$4) {
		if ($$3.getBlock() != $$0.getBlock() && $$1 instanceof ServerLevel $$5) {
			this.checkAndFlip($$0, $$5, $$2);
		}
	}

	@Override
	protected void neighborChanged(BlockState $$0, Level $$1, BlockPos $$2, Block $$3, BlockPos $$4, boolean $$5) {
		if ($$1 instanceof ServerLevel $$6) {
			this.checkAndFlip($$0, $$6, $$2);
		}

	}

	public void checkAndFlip(BlockState state, ServerLevel level, BlockPos pos) {
		boolean neighbor = level.hasNeighborSignal(pos);
		if (neighbor == state.getValue(POWERED))
			return;

		if (!state.getValue(POWERED)) {
			state = state.cycle(FOCUS);
			level.playSound(null, pos, state.getValue(FOCUS).focused() ? SoundEvents.COPPER_BULB_TURN_ON : SoundEvents.COPPER_BULB_TURN_OFF, SoundSource.BLOCKS);
		}

		level.setBlock(pos, state.setValue(POWERED, neighbor), 3);
	}

	public boolean shouldBlockBeacon(BlockState state) {
		return state.getValue(CopperLensBlock.AXIS) != Direction.Axis.Y
			|| state.getValue(CopperLensBlock.FOCUS) != CopperLensBlock.Focus.FOCUSED;
	}

	public enum Focus implements StringRepresentable {
		FOCUSED("focused"),
		UNFOCUSED("unfocused");

		private final String name;

		Focus(String name) {
			this.name = name;
		}

		public boolean focused() {
			return this == FOCUSED;
		}

		@Override
		public @NotNull String getSerializedName() {
			return name;
		}

		@Override
		public String toString() {
			return name;
		}
	}
}
