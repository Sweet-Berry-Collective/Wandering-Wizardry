package dev.sweetberry.wwizardry.block.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.recipe.Recipe;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public abstract class AltarBlockEntity extends BlockEntity implements Inventory {
	private EndCrystalEntity endCrystalEntity;

	public float rand = (float) (Math.floor(Math.random() * Math.PI * 4) / 4);

	public ItemStack heldItem = ItemStack.EMPTY;

	public boolean crafting = false;
	public int craftingTick = 0;

	public AltarBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public EndCrystalEntity getOrCreateEndCrystalEntity() {
		if (endCrystalEntity != null)
			return endCrystalEntity;

		endCrystalEntity = EntityType.END_CRYSTAL.create(world);
		endCrystalEntity.setShowBottom(false);
		return endCrystalEntity;
	}

	public void update() {
		markDirty();
		world.updateNeighbors(pos, getBlock());
	}

	public void startCrafting(@Nullable Recipe<?> recipe) {
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
			dropContainedItems(heldItem);
			heldItem = heldItem.getRecipeRemainder();
		}
		update();
	}

	public void dropContainedItems(ItemStack stack) {
		if (world == null)
			return;
		getBundledStacks(stack).forEach(it -> {
			if (it.isEmpty())
				return;
			var center = pos.ofCenter();
			var entity = new ItemEntity(world, center.x, center.y+1, center.z, it);
			world.spawnEntity(entity);
		});
	}

	private static Stream<ItemStack> getBundledStacks(ItemStack stack) {
		NbtCompound nbtCompound = stack.getNbt();
		if (nbtCompound == null)
			return Stream.empty();
		if (!nbtCompound.contains("Items"))
			return Stream.empty();
		NbtList nbtList = nbtCompound.getList("Items", NbtElement.COMPOUND_TYPE);
		return nbtList.stream().map(NbtCompound.class::cast).map(ItemStack::fromNbt);
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
