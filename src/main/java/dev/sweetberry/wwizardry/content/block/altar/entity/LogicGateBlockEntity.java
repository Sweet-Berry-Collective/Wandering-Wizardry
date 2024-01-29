package dev.sweetberry.wwizardry.content.block.altar.entity;

import dev.sweetberry.wwizardry.content.block.BlockInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

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
	protected void writeNbt(NbtCompound nbt) {
		nbt.putInt("OutputSignal", this.outputSignal);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		this.outputSignal = nbt.getInt("OutputSignal");
	}

	public int getOutputSignal() {
		return this.outputSignal;
	}

	public void setOutputSignal(int outputSignal) {
		this.outputSignal = outputSignal;
	}
}
