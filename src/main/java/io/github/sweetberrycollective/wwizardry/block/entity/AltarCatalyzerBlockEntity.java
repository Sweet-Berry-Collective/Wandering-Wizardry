package io.github.sweetberrycollective.wwizardry.block.entity;

import io.github.sweetberrycollective.wwizardry.block.AltarCatalyzerBlock;
import io.github.sweetberrycollective.wwizardry.block.AltarPedestalBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.quiltmc.qsl.block.entity.api.QuiltBlockEntityTypeBuilder;

public class AltarCatalyzerBlockEntity extends BlockEntity {
	public static final BlockEntityType<AltarCatalyzerBlockEntity> TYPE = QuiltBlockEntityTypeBuilder.create(AltarCatalyzerBlockEntity::new, AltarCatalyzerBlock.INSTANCE).build();
	public AltarCatalyzerBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state);
	}

	public void tick(World world, BlockPos pos, BlockState state) {

	}
}
