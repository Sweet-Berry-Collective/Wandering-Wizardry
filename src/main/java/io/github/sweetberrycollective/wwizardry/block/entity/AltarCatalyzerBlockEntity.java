package io.github.sweetberrycollective.wwizardry.block.entity;

import io.github.sweetberrycollective.wwizardry.block.AltarCatalyzerBlock;
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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.qsl.block.entity.api.QuiltBlockEntityTypeBuilder;

import java.util.ArrayList;

public class AltarCatalyzerBlockEntity extends BlockEntity implements Inventory {
	public float rand = (float)(Math.floor(Math.random()*Math.PI*4)/4);

	public ItemStack heldItem = ItemStack.EMPTY;

	public static final BlockEntityType<AltarCatalyzerBlockEntity> TYPE = QuiltBlockEntityTypeBuilder.create(AltarCatalyzerBlockEntity::new, AltarCatalyzerBlock.INSTANCE).build();
	public AltarCatalyzerBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state);
	}

	public void tick(World world, BlockPos pos, BlockState state) {

	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		var compound = new NbtCompound();
		heldItem.writeNbt(compound);
		nbt.put("HeldItem", compound);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		var compound = nbt.getCompound("HeldItem");
		heldItem = ItemStack.fromNbt(compound);
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

	public ArrayList<AltarPedestalBlockEntity> getNeighbors() {
		var out = new ArrayList<AltarPedestalBlockEntity>();
		if (world == null) return out;
		var north = world.getBlockEntity(pos.north(2));
		var northState = world.getBlockState(pos.north(2));
		if (north instanceof AltarPedestalBlockEntity be && northState.get(HorizontalFacingBlock.FACING) == Direction.NORTH) out.add(be);
		var south = world.getBlockEntity(pos.south(2));
		var southState = world.getBlockState(pos.south(2));
		if (south instanceof AltarPedestalBlockEntity be && southState.get(HorizontalFacingBlock.FACING) == Direction.SOUTH) out.add(be);
		var east = world.getBlockEntity(pos.east(2));
		var eastState = world.getBlockState(pos.east(2));
		if (east instanceof AltarPedestalBlockEntity be && eastState.get(HorizontalFacingBlock.FACING) == Direction.EAST) out.add(be);
		var west = world.getBlockEntity(pos.west(2));
		var westState = world.getBlockState(pos.east(2));
		if (west instanceof AltarPedestalBlockEntity be && westState.get(HorizontalFacingBlock.FACING) == Direction.WEST) out.add(be);
		return out;
	}

	@Override
	public int size() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return heldItem == ItemStack.EMPTY;
	}

	@Override
	public ItemStack getStack(int slot) {
		var item = heldItem.copy();
		item.setCount(64);
		return item;
	}

	@Override
	public ItemStack removeStack(int slot, int amount) {
		var item = heldItem.copy();
		heldItem = null;
		return item;
	}

	@Override
	public ItemStack removeStack(int slot) {
		var item = heldItem.copy();
		heldItem = null;
		return item;
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		heldItem = stack.copy();
		heldItem.setCount(1);
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
