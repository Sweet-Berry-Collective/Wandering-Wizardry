package io.github.sweetberrycollective.wwizardry.block.entity;

import io.github.sweetberrycollective.wwizardry.block.AltarPedestalBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.qsl.block.entity.api.QuiltBlockEntityTypeBuilder;

import java.util.Optional;

public class AltarPedestalBlockEntity extends AltarBlockEntity {

	public Vec3f particlePos;
	public int particleX;
	public int particleZ;

	public static final BlockEntityType<AltarPedestalBlockEntity> TYPE = QuiltBlockEntityTypeBuilder.create(AltarPedestalBlockEntity::new, AltarPedestalBlock.INSTANCE).build();

	public AltarPedestalBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state);
		Direction dir = state.get(HorizontalFacingBlock.FACING);
		// TODO deverbosify
		particlePos = switch (dir) {
			case NORTH ->
					new Vec3f((float) (pos.getX() + 0.46645), (float) (pos.getY() + 0.9229), (float) (pos.getZ() + 0.5));
			case SOUTH ->
					new Vec3f((float) (pos.getX() + 0.53355), (float) (pos.getY() + 0.9229), (float) (pos.getZ() + 0.5));
			case EAST ->
					new Vec3f((float) (pos.getX() + 0.5), (float) (pos.getY() + 0.9229), (float) (pos.getZ() + 0.53355));
			case WEST ->
					new Vec3f((float) (pos.getX() + 0.5), (float) (pos.getY() + 0.9229), (float) (pos.getZ() + 0.46645));
			default -> Vec3f.ZERO;
		};
		particleX = dir == Direction.EAST ? -1 : dir == Direction.WEST ? 1 : 0;
		particleZ = dir == Direction.NORTH ? 1 : dir == Direction.SOUTH ? -1 : 0;
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
	public void tryCraft() {
		tryCraft(world.getBlockState(pos));
	}

	public void tryCancelCraft(BlockState state) {
		findCatalyzer(state).ifPresent(AltarCatalyzerBlockEntity::cancelCraft);
	}

	@Override
	public Block getBlock() {
		return AltarPedestalBlock.INSTANCE;
	}

	public void emitCraftingParticle(World world) {
		world.addParticle(
				ParticleTypes.SOUL_FIRE_FLAME, particlePos.getX(), particlePos.getY(), particlePos.getZ(),
				0.10355 * particleX * ((craftingTick + 30) / 100f),
				0.25 * ((craftingTick + 30) / 100f),
				0.10355 * particleZ * ((craftingTick + 30) / 100f)
		);
	}

	@Override
	public void tick(World world, BlockPos pos, BlockState state) {

		if (crafting) {
			if (++craftingTick >= 100) {
				finishCrafting(null, true);
			}
			emitCraftingParticle(world);
		}
		else {
			craftingTick = 0;
		}
	}
}
