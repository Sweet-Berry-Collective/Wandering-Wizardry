package io.github.sweetberrycollective.wwizardry.block.entity;

import io.github.sweetberrycollective.wwizardry.recipe.AltarCatalyzationRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class AltarBlockEntity extends BlockEntity implements Inventory {

	public float rand = (float) (Math.floor(Math.random() * Math.PI * 4) / 4);

	public ItemStack heldItem = ItemStack.EMPTY;

	public boolean crafting = false;
	public int craftingTick = 0;

	public AltarBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public void update() {
		markDirty();
		world.updateNeighbors(pos, getBlock());
	}

	public void startCrafting(AltarCatalyzationRecipe recipe) {
		crafting = true;
		craftingTick = 0;
		update();
	}

	public void startCrafting() {
		startCrafting(null);
	}

	public void tryCraft() {
		tryCraft(world.getBlockState(pos));
	}

	public abstract void tryCraft(BlockState state);

	public void cancelCraft() {
		crafting = false;
		craftingTick = 0;
		world.updateNeighbors(pos, getBlock());
	}

	public void tryCancelCraft(BlockState state) {
		cancelCraft();
	}

	public void finishCrafting(BlockState state, boolean removeHeldItem) {
		craftingTick = 0;
		crafting = false;
		if (removeHeldItem) {
			heldItem = ItemStack.EMPTY;
		}
		update();
	}

	public abstract Block getBlock();

	public abstract void tick(World world, BlockPos pos, BlockState state);

	@Override
	protected void writeNbt(NbtCompound nbt) {
		var compound = new NbtCompound();
		heldItem.writeNbt(compound);
		nbt.put("HeldItem", compound);
		nbt.putBoolean("crafting", crafting);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		var compound = nbt.getCompound("HeldItem");
		heldItem = ItemStack.fromNbt(compound);
		crafting = nbt.getBoolean("crafting");
	}

	@Override
	public NbtCompound toSyncedNbt() {
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
		markDirty();
		return item;
	}

	@Override
	public ItemStack removeStack(int slot) {
		if (crafting) return ItemStack.EMPTY;
		var item = heldItem.copy();
		heldItem = ItemStack.EMPTY;
		markDirty();
		return item;
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		if (crafting) return;
		heldItem = stack.copy();
		heldItem.setCount(1);
		tryCraft();
		markDirty();
	}

	@Override
	public void markDirty() {
		if (world == null) return;
		if (world.isClient()) {
			MinecraftClient.getInstance().worldRenderer.scheduleBlockRenders(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());
		} else {
			((ServerWorld) world).getChunkManager().markForUpdate(pos);
		}
		super.markDirty();
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
