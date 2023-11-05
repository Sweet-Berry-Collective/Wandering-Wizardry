package dev.sweetberry.wwizardry.block.entity;

import dev.sweetberry.wwizardry.api.altar.AltarRecipeView;
import dev.sweetberry.wwizardry.block.AltarPedestalBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.joml.Vector3f;
import org.quiltmc.qsl.block.entity.api.QuiltBlockEntityTypeBuilder;

import java.util.Optional;

public class AltarPedestalBlockEntity extends AltarBlockEntity {
	public static final BlockEntityType<AltarPedestalBlockEntity> TYPE = QuiltBlockEntityTypeBuilder.create(AltarPedestalBlockEntity::new, AltarPedestalBlock.INSTANCE).build();

	public Vector3f particlePos;
	public int particleX;
	public int particleZ;

	public AltarPedestalBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state);
		Direction dir = state.get(HorizontalFacingBlock.FACING);

		final var offX = dir.getOffsetX();
		final var offZ = dir.getOffsetZ();

		particlePos = new Vector3f(
			pos.getX() + 0.5f + (offZ * 0.03355f),
			pos.getY() + 0.9229f,
			pos.getZ() + 0.5f + (offX * 0.03355f)
		);

		particleX = -offX;
		particleZ = -offZ;
	}

	public Optional<AltarCatalyzerBlockEntity> findCatalyzer(BlockState state) {
		var dir = state.get(HorizontalFacingBlock.FACING);
		var pos = this.pos.offset(dir, -2);
		return world.getBlockEntity(pos) instanceof AltarCatalyzerBlockEntity catalyzer
				? Optional.of(catalyzer)
				: Optional.empty();
	}

	public void tryCraft(BlockState state) {
		findCatalyzer(state).ifPresent(AltarCatalyzerBlockEntity::tryCraft);
	}

	@Override
	public AltarRecipeView.AltarDirection getDirection(BlockState state) {
		return AltarRecipeView.AltarDirection.fromDirection(state.get(HorizontalFacingBlock.FACING));
	}

	@Override
	public void tryCancelCraft(BlockState state) {
		findCatalyzer(state).ifPresent(AltarCatalyzerBlockEntity::cancelCraft);
	}

	@Override
	public Block getBlock() {
		return AltarPedestalBlock.INSTANCE;
	}

	public void emitCraftingParticle(World world) {
		world.addParticle(
				ParticleTypes.SOUL_FIRE_FLAME, particlePos.x, particlePos.y, particlePos.z,
				0.10355 * particleX * ((craftingTick + 30) / 100f),
				0.25 * ((craftingTick + 30) / 100f),
				0.10355 * particleZ * ((craftingTick + 30) / 100f)
		);
	}

	@Override
	public void tick(World world, BlockPos pos, BlockState state) {
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
