package dev.sweetberry.wwizardry.block.entity;

import dev.sweetberry.wwizardry.block.AltarPedestalBlock;
import dev.sweetberry.wwizardry.block.WanderingBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import org.quiltmc.qsl.block.entity.api.QuiltBlockEntityTypeBuilder;

public class ExtensibleComparatorBlockEntity extends BlockEntity {
	public static final BlockEntityType<ExtensibleComparatorBlockEntity> TYPE =
		QuiltBlockEntityTypeBuilder
			.create(
				ExtensibleComparatorBlockEntity::new,
				WanderingBlocks.MODULO_COMPARATOR
			).build();

	private int outputSignal = 0;

	public ExtensibleComparatorBlockEntity(BlockPos pos, BlockState state) {
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
