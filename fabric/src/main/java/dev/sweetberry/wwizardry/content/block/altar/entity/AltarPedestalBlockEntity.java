package dev.sweetberry.wwizardry.content.block.altar.entity;

import dev.sweetberry.wwizardry.api.altar.AltarRecipeView;
import dev.sweetberry.wwizardry.content.block.altar.AltarPedestalBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3f;

import java.util.Optional;

public class AltarPedestalBlockEntity extends AltarBlockEntity {
	public static final BlockEntityType<AltarPedestalBlockEntity> TYPE = FabricBlockEntityTypeBuilder.create(AltarPedestalBlockEntity::new, AltarPedestalBlock.INSTANCE).build();

	public Vector3f particlePos;
	public int particleX;
	public int particleZ;

	public AltarPedestalBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state);
		Direction dir = state.getValue(HorizontalDirectionalBlock.FACING);

		final var offX = dir.getStepX();
		final var offZ = dir.getStepZ();

		particlePos = new Vector3f(
			pos.getX() + 0.5f + (offZ * 0.03355f),
			pos.getY() + 0.9229f,
			pos.getZ() + 0.5f + (offX * 0.03355f)
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
		return AltarPedestalBlock.INSTANCE;
	}

	public void emitCraftingParticle(Level world) {
		world.addParticle(
				ParticleTypes.SOUL_FIRE_FLAME, particlePos.x, particlePos.y, particlePos.z,
				0.10355 * particleX * ((craftingTick + 30) / 100f),
				0.25 * ((craftingTick + 30) / 100f),
				0.10355 * particleZ * ((craftingTick + 30) / 100f)
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
