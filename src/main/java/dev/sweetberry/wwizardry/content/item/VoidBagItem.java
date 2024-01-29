package dev.sweetberry.wwizardry.content.item;

import dev.sweetberry.wwizardry.compat.component.VoidBagComponent;
import dev.sweetberry.wwizardry.content.net.packet.VoidBagPayload;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;
import org.quiltmc.qsl.networking.api.PacketByteBufs;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

public class VoidBagItem extends Item {
	public static final VoidBagItem INSTANCE = new VoidBagItem(
		new QuiltItemSettings()
			.maxCount(1)
	);

	public VoidBagItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		var bag = VoidBagComponent.getForPlayer(user);
		if (user.isSneaking()) {
			bag.locked = !bag.locked;
			if (world.isClient)
				world.playSound(user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.PLAYERS, 1.0f, 1.0f, false);
			return TypedActionResult.success(user.getStackInHand(hand), world.isClient);
		}
		if (!world.isClient)
			bag.openScreen();

		return TypedActionResult.success(user.getStackInHand(hand), world.isClient);
	}

	@Override
	public boolean onClicked(ItemStack thisStack, ItemStack otherStack, Slot thisSlot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
		if (clickType != ClickType.RIGHT)
			return false;
		if (!player.getWorld().isClient)
			return false;
		if (!otherStack.isEmpty())
			return false;

		ClientPlayNetworking.send(VoidBagPayload.ID, PacketByteBufs.create());

		return true;
	}
}
