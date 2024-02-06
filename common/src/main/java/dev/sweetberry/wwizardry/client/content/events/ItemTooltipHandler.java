package dev.sweetberry.wwizardry.client.content.events;

import dev.sweetberry.wwizardry.content.component.ComponentInitializer;
import dev.sweetberry.wwizardry.content.component.VoidBagComponent;
import dev.sweetberry.wwizardry.content.item.ItemInitializer;
import dev.sweetberry.wwizardry.content.item.SoulMirrorItem;
import dev.sweetberry.wwizardry.content.item.VoidBagItem;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class ItemTooltipHandler {

	public static final List<Component> VOID_BAG_LOCKED = List.of(
		Component.empty(),
		Component.translatable("wwizardry.void_bag.generic_1").withStyle(ChatFormatting.DARK_PURPLE),
		Component.translatable("wwizardry.void_bag.generic_2").withStyle(ChatFormatting.DARK_PURPLE),
		Component.empty(),
		Component.translatable("wwizardry.void_bag.locked_1").withStyle(ChatFormatting.DARK_PURPLE),
		Component.translatable("wwizardry.void_bag.locked_2").withStyle(ChatFormatting.DARK_PURPLE),
		Component.translatable("wwizardry.void_bag.locked_3").withStyle(ChatFormatting.DARK_PURPLE)
	);
	public static final List<Component> VOID_BAG_UNLOCKED = List.of(
		Component.empty(),
		Component.translatable("wwizardry.void_bag.generic_1").withStyle(ChatFormatting.DARK_PURPLE),
		Component.translatable("wwizardry.void_bag.generic_2").withStyle(ChatFormatting.DARK_PURPLE),
		Component.empty(),
		Component.translatable("wwizardry.void_bag.unlocked_1").withStyle(ChatFormatting.DARK_PURPLE),
		Component.translatable("wwizardry.void_bag.unlocked_2").withStyle(ChatFormatting.DARK_PURPLE),
		Component.translatable("wwizardry.void_bag.unlocked_3").withStyle(ChatFormatting.DARK_PURPLE)
	);

	public static final List<Component> SOUL_MIRROR = List.of(
		Component.empty(),
		Component.translatable("wwizardry.soul_mirror.generic_1").withStyle(ChatFormatting.DARK_PURPLE),
		Component.translatable("wwizardry.soul_mirror.generic_2").withStyle(ChatFormatting.DARK_PURPLE)
	);

	public static final List<Component> SOUL_MIRROR_BROKEN = List.of(
		Component.empty(),
		Component.translatable("wwizardry.soul_mirror.generic_1").withStyle(ChatFormatting.DARK_PURPLE),
		Component.translatable("wwizardry.soul_mirror.generic_2").withStyle(ChatFormatting.DARK_PURPLE),
		Component.empty(),
		Component.translatable("wwizardry.soul_mirror.broken").withStyle(ChatFormatting.DARK_PURPLE)
	);

	public static void addTooltips(ItemStack stack, TooltipFlag context, List<Component> lines) {
		if (stack.is(ItemInitializer.VOID_BAG.get())) {
			var player = Minecraft.getInstance().player;
			if (player == null)
				return;
			var bag = ComponentInitializer.<VoidBagComponent>getComponent(ComponentInitializer.VOID_BAG, player);
			lines.addAll(
				1,
				bag.locked ? VOID_BAG_LOCKED : VOID_BAG_UNLOCKED
			);
			return;
		}
		if (stack.is(ItemInitializer.SOUL_MIRROR.get())) {
			lines.addAll(
				1,
				ItemInitializer.SOUL_MIRROR.get().isFullyUsed(stack) ? SOUL_MIRROR_BROKEN : SOUL_MIRROR
			);
		}
	}
}
