package dev.sweetberry.wwizardry.content.block.altar.entity;

import dev.sweetberry.wwizardry.api.altar.AltarRecipeView;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AltarBlockEntity extends BlockEntity implements Container {
	private EndCrystal endCrystalEntity;

	public float rand = (float) (Math.floor(Math.random() * Math.PI * 4) / 4);

	public ItemStack heldItem = ItemStack.EMPTY;

	public ItemStack recipeRemainder = ItemStack.EMPTY;

	public boolean crafting = false;
	public int craftingTick = 0;

	public AltarBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public EndCrystal getOrCreateEndCrystalEntity() {
		if (endCrystalEntity != null)
			return endCrystalEntity;

		endCrystalEntity = EntityType.END_CRYSTAL.create(level);
		endCrystalEntity.setShowBottom(false);
		return endCrystalEntity;
	}

	public void update() {
		setChanged();
		level.blockUpdated(worldPosition, getBlock());
	}

	public void startCrafting(AltarRecipeView recipe) {
		crafting = true;
		craftingTick = 0;
		recipeRemainder = recipe.getResultInPedestal(getDirection(level.getBlockState(worldPosition)));
		update();
	}

	public void tryCraft() {
		tryCraft(level.getBlockState(worldPosition));
	}

	public abstract void tryCraft(BlockState state);

	public abstract AltarRecipeView.AltarDirection getDirection(BlockState state);

	public void cancelCraft() {
		crafting = false;
		craftingTick = 0;
		level.blockUpdated(worldPosition, getBlock());
	}

	public void tryCancelCraft(BlockState state) {
		cancelCraft();
	}

	public void finishCrafting(BlockState state) {
		craftingTick = 0;
		crafting = false;
		dropContainedItems(heldItem);
		heldItem = recipeRemainder;
		recipeRemainder = ItemStack.EMPTY;
		update();
	}

	public void dropContainedItems(ItemStack stack) {
		if (level == null)
			return;
		if (stack.isEmpty())
			return;
		getBundledStacks(stack).forEach(it -> {
			if (it.isEmpty())
				return;
			var center = worldPosition.getCenter();
			var entity = new ItemEntity(level, center.x, center.y+1, center.z, it);
			level.addFreshEntity(entity);
		});
	}

	private static Stream<ItemStack> getBundledStacks(ItemStack stack) {
		CompoundTag nbtCompound = stack.getTag();
		if (nbtCompound == null)
			return Stream.empty();
		if (!nbtCompound.contains("Items"))
			return Stream.empty();
		ListTag nbtList = nbtCompound.getList("Items", Tag.TAG_COMPOUND);
		return nbtList.stream().map(CompoundTag.class::cast).map(ItemStack::of);
	}

	public abstract Block getBlock();

	public abstract void tick(Level world, BlockPos pos, BlockState state);

	@Override
	protected void saveAdditional(CompoundTag nbt) {
		var heldItemNbt = new CompoundTag();
		heldItem.save(heldItemNbt);
		var recipeRemainderNbt = new CompoundTag();
		recipeRemainder.save(recipeRemainderNbt);
		nbt.put("HeldItem", heldItemNbt);
		nbt.put("RecipeRemainder", recipeRemainderNbt);
		nbt.putBoolean("crafting", crafting);
	}

	@Override
	public void load(CompoundTag nbt) {
		var heldItemNbt = nbt.getCompound("HeldItem");
		heldItem = ItemStack.of(heldItemNbt);
		var recipeRemainderNbt = nbt.getCompound("RecipeRemainder");
		recipeRemainder = ItemStack.of(recipeRemainderNbt);
		crafting = nbt.getBoolean("crafting");
	}

	@Override
	public CompoundTag getUpdateTag() {
		return saveWithoutMetadata();
	}

	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public int getContainerSize() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return heldItem == ItemStack.EMPTY || crafting;
	}

	@Override
	public ItemStack getItem(int slot) {
		if (crafting) return ItemStack.EMPTY;
		var item = heldItem.copy();
		item.setCount(64);
		return item;
	}

	@Override
	public ItemStack removeItem(int slot, int amount) {
		if (crafting) return ItemStack.EMPTY;
		var item = heldItem.copy();
		heldItem = ItemStack.EMPTY;
		setChanged();
		return item;
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		if (crafting) return ItemStack.EMPTY;
		var item = heldItem.copy();
		heldItem = ItemStack.EMPTY;
		setChanged();
		return item;
	}

	@Override
	public void setItem(int slot, ItemStack stack) {
		if (crafting) return;
		heldItem = stack.copy();
		heldItem.setCount(1);
		tryCraft();
		setChanged();
	}

	@Override
	public void setChanged() {
		if (level == null) return;
		if (level.isClientSide()) {
			Minecraft.getInstance().levelRenderer.setBlocksDirty(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), worldPosition.getX(), worldPosition.getY(), worldPosition.getZ());
		} else {
			((ServerLevel) level).getChunkSource().blockChanged(worldPosition);
		}
		super.setChanged();
	}

	@Override
	public boolean stillValid(Player player) {
		return false;
	}

	@Override
	public void clearContent() {
		heldItem = ItemStack.EMPTY;
	}
}
