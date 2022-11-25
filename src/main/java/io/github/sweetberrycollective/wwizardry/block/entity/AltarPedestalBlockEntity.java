package io.github.sweetberrycollective.wwizardry.block.entity;

import io.github.sweetberrycollective.wwizardry.block.AltarPedestalBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.quiltmc.qsl.block.entity.api.QuiltBlockEntityTypeBuilder;

public class AltarPedestalBlockEntity extends BlockEntity {
	public float rand = (float)(Math.floor(Math.random()*Math.PI*4)/4);
	public static final BlockEntityType<AltarPedestalBlockEntity> TYPE = QuiltBlockEntityTypeBuilder.create(AltarPedestalBlockEntity::new, AltarPedestalBlock.INSTANCE).build();
	public AltarPedestalBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state);
	}

	public void tick(World world, BlockPos pos, BlockState state) {

	}
}
