package dev.sweetberry.wwizardry.content.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.sweetberry.wwizardry.api.component.Component;
import dev.sweetberry.wwizardry.content.item.ItemInitializer;
import dev.sweetberry.wwizardry.content.item.VoidBagItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.ChestBlockEntity;

import java.util.List;

public class VoidBagComponent implements Component<VoidBagComponent>, Container {
	public static final Codec<VoidBagComponent> CODEC = RecordCodecBuilder.create(inst -> inst
		.group(
			Codec.BOOL.fieldOf("locked").forGetter(it -> it.locked),
			ItemStack.OPTIONAL_CODEC.sizeLimitedListOf(27).fieldOf("items").forGetter(it -> it.inventory)
		).apply(inst, VoidBagComponent::new)
	);

	public NonNullList<ItemStack> inventory = NonNullList.withSize(27, ItemStack.EMPTY);
	public boolean locked = false;

	public VoidBagComponent(boolean locked, List<ItemStack> inventory) {
		this.locked = locked;
		for (int i = 0; i < 27; i++)
			this.inventory.set(i, inventory.get(i));
	}

    public VoidBagComponent() {}

	@Override
	public Codec<VoidBagComponent> codec() {
		return CODEC;
	}

	@Override
	public void copyFrom(VoidBagComponent other) {
		locked = other.locked;
		inventory = other.inventory;
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

			if (!ItemStack.isSameItemSameComponents(inv_stack, stack))
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
