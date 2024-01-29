package dev.sweetberry.wwizardry.client.content.events;

import dev.sweetberry.wwizardry.compat.component.VoidBagComponent;
import dev.sweetberry.wwizardry.content.item.SoulMirrorItem;
import dev.sweetberry.wwizardry.content.item.VoidBagItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class ModelPredicates {
	public static float getVoidBag(ItemStack itemStack, ClientWorld clientWorld, LivingEntity livingEntity, int i) {
		if (!itemStack.isOf(VoidBagItem.INSTANCE)) return 0.0f;
		var client = MinecraftClient.getInstance();
		if (client.player == null)
			return 0.0f;
		var nbt = itemStack.getNbt();
		if (nbt != null && nbt.contains("Locked")) return nbt.getBoolean("Locked") ? 1.0f : 0.0f;
		var bag = VoidBagComponent.getForPlayer(client.player);
		return bag.locked ? 1 : 0;
	}

	public static float getSoulMirror(ItemStack itemStack, ClientWorld clientWorld, LivingEntity livingEntity, int i) {
		return SoulMirrorItem.INSTANCE.isFullyUsed(itemStack) ? 1 : 0;
	}
}
