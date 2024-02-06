package dev.sweetberry.wwizardry.client.content.events;

import dev.sweetberry.wwizardry.content.component.ComponentInitializer;
import dev.sweetberry.wwizardry.content.component.VoidBagComponent;
import dev.sweetberry.wwizardry.content.item.ItemInitializer;
import dev.sweetberry.wwizardry.content.item.SoulMirrorItem;
import dev.sweetberry.wwizardry.content.item.VoidBagItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class ModelPredicates {
	public static float getVoidBag(ItemStack itemStack, ClientLevel clientWorld, LivingEntity livingEntity, int i) {
		if (!itemStack.is(ItemInitializer.VOID_BAG.get())) return 0.0f;
		var client = Minecraft.getInstance();
		if (client.player == null)
			return 0.0f;
		var nbt = itemStack.getTag();
		if (nbt != null && nbt.contains("Locked")) return nbt.getBoolean("Locked") ? 1.0f : 0.0f;
		var bag = ComponentInitializer.<VoidBagComponent>getComponent(ComponentInitializer.VOID_BAG, client.player);
		return bag.locked ? 1 : 0;
	}

	public static float getSoulMirror(ItemStack itemStack, ClientLevel clientWorld, LivingEntity livingEntity, int i) {
		return ItemInitializer.SOUL_MIRROR.get().isFullyUsed(itemStack) ? 1 : 0;
	}
}
