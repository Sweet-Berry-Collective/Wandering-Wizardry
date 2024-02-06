package dev.sweetberry.wwizardry.content.component;

import dev.sweetberry.wwizardry.api.component.Component;
import dev.sweetberry.wwizardry.content.item.ItemInitializer;
import dev.sweetberry.wwizardry.content.item.VoidBagItem;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class VoidBagComponent implements Component, Container {
	public NonNullList<ItemStack> inventory = NonNullList.withSize(27, ItemStack.EMPTY);
	public boolean locked = false;

    public VoidBagComponent() {}

    @Override
	public void fromNbt(CompoundTag tag) {
		inventory = NonNullList.withSize(27, ItemStack.EMPTY);
		ContainerHelper.loadAllItems(tag, inventory);
		locked = tag.getBoolean("Locked");
	}

	@Override
	public void toNbt(CompoundTag tag) {
		ContainerHelper.saveAllItems(tag, inventory);
		tag.putBoolean("Locked", locked);
		ItemStack previewStack = ItemInitializer.VOID_BAG.get().getDefaultInstance();
		previewStack.getOrCreateTag().putBoolean("Locked", locked);
		CompoundTag previewCompound = new CompoundTag();
		previewStack.save(previewCompound);
		tag.put("PreviewStack", previewCompound);
	}

	@Override
	public int getContainerSize() {
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
	public ItemStack getItem(int slot) {
		return inventory.get(slot);
	}

	@Override
	public ItemStack removeItem(int slot, int amount) {
		ItemStack stack = ContainerHelper.removeItem(inventory, slot, amount);
		if (!stack.isEmpty()) {
			setChanged();
		}
		return stack;
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		return ContainerHelper.takeItem(inventory, slot);
	}

	@Override
	public void setItem(int slot, ItemStack stack) {
		inventory.set(slot, stack.copy());

		this.setChanged();
	}

	@Override
	public void setChanged() {
	}

	@Override
	public boolean stillValid(Player player) {
		return true;
	}

	@Override
	public void clearContent() {
		inventory = NonNullList.withSize(27, ItemStack.EMPTY);
		setChanged();
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
				setItem(i, stack);
				stack.setCount(0);
				return 0;
			}

			if (!ItemStack.isSameItemSameTags(inv_stack, stack))
				continue;

			var count_to_fill = inv_stack.getMaxStackSize() - inv_stack.getCount();

			if (count_to_fill == 0)
				continue;

			var amount = Math.min(count_to_fill, stack.getCount());
			stack.shrink(amount);
			inv_stack.grow(amount);

			if (stack.isEmpty()) {
				return 0;
			}
		}
		return stack.getCount();
	}

	public void openScreen(Player player) {
		var factory = new SimpleMenuProvider((syncid, inventory, _player) -> ChestMenu.threeRows(syncid, inventory, this), net.minecraft.network.chat.Component.translatable("item.wwizardry.void_bag"));

		player.openMenu(factory);
	}
}
