package dev.sweetberry.wwizardry.content.item;

import dev.sweetberry.wwizardry.content.component.ComponentInitializer;
import dev.sweetberry.wwizardry.content.component.VoidBagComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class VoidBagItem extends Item {
	public VoidBagItem(Properties settings) {
		super(settings);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		var bag = ComponentInitializer.<VoidBagComponent>getComponent(ComponentInitializer.VOID_BAG, user);
		if (user.isShiftKeyDown()) {
			bag.locked = !bag.locked;
			if (world.isClientSide)
				world.playLocalSound(user.getX(), user.getY(), user.getZ(), SoundEvents.SHEEP_SHEAR, SoundSource.PLAYERS, 1.0f, 1.0f, false);
			return InteractionResultHolder.sidedSuccess(user.getItemInHand(hand), world.isClientSide);
		}
		if (!world.isClientSide)
			bag.openScreen(user);

		return InteractionResultHolder.sidedSuccess(user.getItemInHand(hand), world.isClientSide);
	}

	@Override
	public boolean overrideOtherStackedOnMe(ItemStack thisStack, ItemStack otherStack, Slot thisSlot, ClickAction clickType, Player player, SlotAccess cursorStackReference) {
		if (clickType != ClickAction.SECONDARY)
			return false;
		if (!otherStack.isEmpty())
			return false;
		if (player.level().isClientSide)
			return true;

		var bag = ComponentInitializer.<VoidBagComponent>getComponent(ComponentInitializer.VOID_BAG, player);;
		bag.openScreen(player);

		return true;
	}
}
