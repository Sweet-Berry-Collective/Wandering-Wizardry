package dev.sweetberry.wwizardry.compat.cardinal.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.entity.PlayerComponent;
import dev.sweetberry.wwizardry.compat.cardinal.CardinalInitializer;
import dev.sweetberry.wwizardry.content.item.VoidBagItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;

public class VoidBagComponent implements Inventory, PlayerComponent<VoidBagComponent>, AutoSyncedComponent {
	public final PlayerEntity player;
	public DefaultedList<ItemStack> inventory = DefaultedList.ofSize(27, ItemStack.EMPTY);
	public boolean locked = false;

	public VoidBagComponent(PlayerEntity player) {
		this.player = player;
	}

	@Override
	public boolean shouldSyncWith(ServerPlayerEntity player) {
		return this.player.equals(player);
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		inventory = DefaultedList.ofSize(27, ItemStack.EMPTY);
		Inventories.readNbt(tag, inventory);
		locked = tag.getBoolean("Locked");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		Inventories.writeNbt(tag, inventory);
		tag.putBoolean("Locked", locked);
		ItemStack previewStack = VoidBagItem.INSTANCE.getDefaultStack();
		previewStack.getOrCreateNbt().putBoolean("Locked", locked);
		NbtCompound previewCompound = new NbtCompound();
		previewStack.writeNbt(previewCompound);
		tag.put("PreviewStack", previewCompound);
	}

	@Override
	public boolean shouldCopyForRespawn(boolean lossless, boolean keepInventory, boolean sameCharacter) {
		return true;
	}

	@Override
	public int size() {
		return 27;
	}

	@Override
	public boolean isEmpty() {
		for (var stack : inventory)
			if (!stack.isEmpty())
				return false;
		return true;
	}

	@Override
	public ItemStack getStack(int slot) {
		return inventory.get(slot);
	}

	@Override
	public ItemStack removeStack(int slot, int amount) {
		ItemStack stack = Inventories.splitStack(inventory, slot, amount);
		if (!stack.isEmpty()) {
			markDirty();
		}
		return stack;
	}

	@Override
	public ItemStack removeStack(int slot) {
		return Inventories.removeStack(inventory, slot);
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		inventory.set(slot, stack.copy());

		this.markDirty();
	}

	@Override
	public void markDirty() {
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return true;
	}

	@Override
	public void clear() {
		inventory = DefaultedList.ofSize(27, ItemStack.EMPTY);
		markDirty();
	}

	public boolean contains(Item item) {
		for (var stack : inventory)
			if (stack.getItem() == item)
				return true;
		return false;
	}

	public int tryAddStack(ItemStack stack) {
		for (var i = 0; i < inventory.size(); i++) {
			var inv_stack = inventory.get(i);
			if (inv_stack.isEmpty()) {
				setStack(i, stack);
				stack.setCount(0);
				return 0;
			}

			if (!ItemStack.canCombine(inv_stack, stack))
				continue;

			var count_to_fill = inv_stack.getMaxCount() - inv_stack.getCount();

			if (count_to_fill == 0)
				continue;

			var amount = Math.min(count_to_fill, stack.getCount());
			stack.decrement(amount);
			inv_stack.increment(amount);

			if (stack.isEmpty()) {
				return 0;
			}
		}
		return stack.getCount();
	}

	public void openScreen() {
		var factory = new SimpleNamedScreenHandlerFactory((syncid, inventory, _player) -> GenericContainerScreenHandler.createGeneric9x3(syncid, inventory, this), Text.translatable("item.wwizardry.void_bag"));

		player.openHandledScreen(factory);
	}

	public static VoidBagComponent getForPlayer(PlayerEntity player) {
		return player.getComponent(CardinalInitializer.VOID_BAG);
	}
}
