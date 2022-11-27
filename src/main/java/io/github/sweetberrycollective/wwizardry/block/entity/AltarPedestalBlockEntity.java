package io.github.sweetberrycollective.wwizardry.block.entity;

import io.github.sweetberrycollective.wwizardry.block.AltarPedestalBlock;
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
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.qsl.block.entity.api.QuiltBlockEntityTypeBuilder;

public class AltarPedestalBlockEntity extends BlockEntity implements Inventory {
	public float rand = (float) (Math.floor(Math.random() * Math.PI * 4) / 4);

	public ItemStack heldItem = ItemStack.EMPTY;

	public boolean crafting = false;
	public int craftingTick = 0;

	public static final BlockEntityType<AltarPedestalBlockEntity> TYPE = QuiltBlockEntityTypeBuilder.create(AltarPedestalBlockEntity::new, AltarPedestalBlock.INSTANCE).build();

	public AltarPedestalBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state);
	}

	public void startCrafting() {
		crafting = true;
		craftingTick = 0;
		markDirty();
		if (world == null) return;
		world.updateNeighbors(pos, AltarPedestalBlock.INSTANCE);
	}

	public void tryCraft(BlockState state) {
		var dir = state.get(HorizontalFacingBlock.FACING);
		var pos = this.pos.offset(dir, -2);
		var entity = world.getBlockEntity(pos);
		if (!(entity instanceof AltarCatalyzerBlockEntity altarEntity)) return;
		altarEntity.tryCraft();
	}

	public void tryCancelCraft(BlockState state) {
		var dir = state.get(HorizontalFacingBlock.FACING);
		var pos = this.pos.offset(dir, -2);
		var entity = world.getBlockEntity(pos);
		if (!(entity instanceof AltarCatalyzerBlockEntity altarEntity)) return;
		altarEntity.cancelCraft();
	}

	public void cancelCraft() {
		crafting = false;
		craftingTick = 0;
	}

	public void tick(World world, BlockPos pos, BlockState state) {
		var dir = state.get(HorizontalFacingBlock.FACING);
		var vpos = switch (dir) {
			case NORTH ->
					new Vec3f((float) (pos.getX() + 0.46645), (float) (pos.getY() + 0.9229), (float) (pos.getZ() + 0.5));
			case SOUTH ->
					new Vec3f((float) (pos.getX() + 0.53355), (float) (pos.getY() + 0.9229), (float) (pos.getZ() + 0.5));
			case EAST ->
					new Vec3f((float) (pos.getX() + 0.5), (float) (pos.getY() + 0.9229), (float) (pos.getZ() + 0.53355));
			case WEST ->
					new Vec3f((float) (pos.getX() + 0.5), (float) (pos.getY() + 0.9229), (float) (pos.getZ() + 0.46645));
			default -> new Vec3f(0, 0, 0);
		};
		var x = switch (dir) {
			case EAST -> -1;
			case WEST -> 1;
			default -> 0;
		};
		var z = switch (dir) {
			case NORTH -> 1;
			case SOUTH -> -1;
			default -> 0;
		};
		if (crafting) {
			if (++craftingTick == 100) {
				craftingTick = 0;
				crafting = false;
				heldItem = ItemStack.EMPTY;
				world.updateNeighbors(pos, state.getBlock());
				markDirty();
			}
			world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, vpos.getX(), vpos.getY(), vpos.getZ(), 0.10355 * x * ((craftingTick + 30) / 100f), 0.25 * ((craftingTick + 30) / 100f), 0.10355 * z * ((craftingTick + 30) / 100f));
		}
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		var compound = new NbtCompound();
		heldItem.writeNbt(compound);
		nbt.put("HeldItem", compound);
		nbt.putBoolean("crafting", crafting);
		nbt.putInt("craftingTick", craftingTick);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		var compound = nbt.getCompound("HeldItem");
		heldItem = ItemStack.fromNbt(compound);
		crafting = nbt.getBoolean("crafting");
		craftingTick = nbt.getInt("craftingTick");
	}

	@Override
	public NbtCompound toInitialChunkDataNbt() {
		return toNbt();
	}

	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.of(this);
	}

	@Override
	public int size() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return heldItem == ItemStack.EMPTY || crafting;
	}

	@Override
	public ItemStack getStack(int slot) {
		if (crafting) return ItemStack.EMPTY;
		var item = heldItem.copy();
		item.setCount(64);
		return item;
	}

	@Override
	public ItemStack removeStack(int slot, int amount) {
		if (crafting) return ItemStack.EMPTY;
		var item = heldItem.copy();
		heldItem = ItemStack.EMPTY;
		return item;
	}

	@Override
	public ItemStack removeStack(int slot) {
		if (crafting) return ItemStack.EMPTY;
		var item = heldItem.copy();
		heldItem = ItemStack.EMPTY;
		return item;
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		if (crafting) return;
		heldItem = stack.copy();
		heldItem.setCount(1);
		tryCraft(world.getBlockState(pos));
	}

	@Override
	public void markDirty() {
		if (world != null) {
			if (world.isClient()) {
				MinecraftClient.getInstance().worldRenderer.scheduleBlockRenders(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());
			} else {
				((ServerWorld) world).getChunkManager().markForUpdate(pos);
			}
			super.markDirty();
		}
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return false;
	}

	@Override
	public void clear() {
		heldItem = ItemStack.EMPTY;
	}
}
