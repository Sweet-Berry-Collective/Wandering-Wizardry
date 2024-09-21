package dev.sweetberry.wwizardry.content.block.entity;

import dev.sweetberry.wwizardry.content.block.BlockInitializer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class LogicGateBlockEntity extends BlockEntity {
	private int outputSignal = 0;

	public LogicGateBlockEntity(BlockPos pos, BlockState state) {
		super(BlockInitializer.LOGIC_GATE_TYPE.get(), pos, state);
	}

	@Override
	public void saveAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
		nbt.putInt("OutputSignal", this.outputSignal);
	}

	@Override
	public void loadAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
		this.outputSignal = nbt.getInt("OutputSignal");
	}

	public int getOutputSignal() {
		return this.outputSignal;
	}

	public void setOutputSignal(int outputSignal) {
		this.outputSignal = outputSignal;
	}
}
