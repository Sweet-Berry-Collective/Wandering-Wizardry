package dev.sweetberry.wwizardry.content.block.entity;

import dev.sweetberry.wwizardry.content.block.BlockInitializer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class LogicGateBlockEntity extends BlockEntity {
	public static final BlockEntityType<LogicGateBlockEntity> TYPE =
		BlockEntityType.Builder
			.of(
				LogicGateBlockEntity::new,
				BlockInitializer.MODULO_COMPARATOR
			).build(null);

	private int outputSignal = 0;

	public LogicGateBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state);
	}

	@Override
	protected void saveAdditional(CompoundTag nbt) {
		nbt.putInt("OutputSignal", this.outputSignal);
	}

	@Override
	public void load(CompoundTag nbt) {
		this.outputSignal = nbt.getInt("OutputSignal");
	}

	public int getOutputSignal() {
		return this.outputSignal;
	}

	public void setOutputSignal(int outputSignal) {
		this.outputSignal = outputSignal;
	}
}
