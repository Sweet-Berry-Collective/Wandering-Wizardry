package dev.sweetberry.wwizardry.content.block.altar.entity;

import dev.sweetberry.wwizardry.content.block.BlockInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class LogicGateBlockEntity extends BlockEntity {
	public static final BlockEntityType<LogicGateBlockEntity> TYPE =
		FabricBlockEntityTypeBuilder
			.create(
				LogicGateBlockEntity::new,
				BlockInitializer.MODULO_COMPARATOR
			).build();

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
