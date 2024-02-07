package dev.sweetberry.wwizardry.content.block.entity;

import dev.sweetberry.wwizardry.api.altar.AltarRecipeView;
import dev.sweetberry.wwizardry.content.block.BlockInitializer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3f;

import java.util.Optional;

public class AltarPedestalBlockEntity extends AltarBlockEntity {
	private static final double ALTAR_TOP = 16.1854d / 16d;
	private static final double ALTAR_OFFSET = 1.0306d / 16d;

	public Vector3f particlePos;
	public int particleX;
	public int particleZ;

	public AltarPedestalBlockEntity(BlockPos pos, BlockState state) {
		super(BlockInitializer.ALTAR_PEDESTAL_TYPE.get(), pos, state);
		Direction dir = state.getValue(HorizontalDirectionalBlock.FACING);

		final var offX = dir.getStepX();
		final var offZ = dir.getStepZ();

		particlePos = new Vector3f(
			pos.getX() + 0.5f + (float)(offZ * ALTAR_OFFSET),
			pos.getY() + (float)ALTAR_TOP,
			pos.getZ() + 0.5f + (float)(offX * ALTAR_OFFSET)
		);

		particleX = -offX;
		particleZ = -offZ;
	}

	public Optional<AltarCatalyzerBlockEntity> findCatalyzer(BlockState state) {
		var dir = state.getValue(HorizontalDirectionalBlock.FACING);
		var pos = this.worldPosition.relative(dir, -2);
		return level.getBlockEntity(pos) instanceof AltarCatalyzerBlockEntity catalyzer
				? Optional.of(catalyzer)
				: Optional.empty();
	}

	public void tryCraft(BlockState state) {
		findCatalyzer(state).ifPresent(AltarCatalyzerBlockEntity::tryCraft);
	}

	@Override
	public AltarRecipeView.AltarDirection getDirection(BlockState state) {
		return AltarRecipeView.AltarDirection.fromDirection(state.getValue(HorizontalDirectionalBlock.FACING));
	}

	@Override
	public void tryCancelCraft(BlockState state) {
		findCatalyzer(state).ifPresent(AltarCatalyzerBlockEntity::cancelCraft);
	}

	@Override
	public Block getBlock() {
		return BlockInitializer.ALTAR_PEDESTAL.get();
	}

	public void emitCraftingParticle(Level world) {
		var xz = clampLerpTime(0.3f, 0, 2 - (float)ALTAR_OFFSET) * timingMultiplier;
		world.addParticle(
			ParticleTypes.SOUL_FIRE_FLAME,
			particlePos.x,
			particlePos.y,
			particlePos.z,
			particleX * xz,
			clampLerpTime(0.3f, 0, 4.25f) * timingMultiplier,
			particleZ * xz
		);
	}

	@Override
	public void tick(Level world, BlockPos pos, BlockState state) {
		if (crafting) {
			if (++craftingTick >= 100) {
				finishCrafting(state);
			}
			emitCraftingParticle(world);
		}
		else {
			craftingTick = 0;
		}
	}
}
