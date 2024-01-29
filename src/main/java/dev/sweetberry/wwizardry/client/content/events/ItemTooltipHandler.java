package dev.sweetberry.wwizardry.client.content.events;

import dev.sweetberry.wwizardry.compat.component.VoidBagComponent;
import dev.sweetberry.wwizardry.content.item.SoulMirrorItem;
import dev.sweetberry.wwizardry.content.item.VoidBagItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class ItemTooltipHandler {

	public static final List<Text> VOID_BAG_LOCKED = List.of(
		Text.empty(),
		Text.translatable("wwizardry.void_bag.generic_1").formatted(Formatting.DARK_PURPLE),
		Text.translatable("wwizardry.void_bag.generic_2").formatted(Formatting.DARK_PURPLE),
		Text.empty(),
		Text.translatable("wwizardry.void_bag.locked_1").formatted(Formatting.DARK_PURPLE),
		Text.translatable("wwizardry.void_bag.locked_2").formatted(Formatting.DARK_PURPLE),
		Text.translatable("wwizardry.void_bag.locked_3").formatted(Formatting.DARK_PURPLE)
	);
	public static final List<Text> VOID_BAG_UNLOCKED = List.of(
		Text.empty(),
		Text.translatable("wwizardry.void_bag.generic_1").formatted(Formatting.DARK_PURPLE),
		Text.translatable("wwizardry.void_bag.generic_2").formatted(Formatting.DARK_PURPLE),
		Text.empty(),
		Text.translatable("wwizardry.void_bag.unlocked_1").formatted(Formatting.DARK_PURPLE),
		Text.translatable("wwizardry.void_bag.unlocked_2").formatted(Formatting.DARK_PURPLE),
		Text.translatable("wwizardry.void_bag.unlocked_3").formatted(Formatting.DARK_PURPLE)
	);

	public static final List<Text> SOUL_MIRROR = List.of(
		Text.empty(),
		Text.translatable("wwizardry.soul_mirror.generic_1").formatted(Formatting.DARK_PURPLE),
		Text.translatable("wwizardry.soul_mirror.generic_2").formatted(Formatting.DARK_PURPLE)
	);

	public static final List<Text> SOUL_MIRROR_BROKEN = List.of(
		Text.empty(),
		Text.translatable("wwizardry.soul_mirror.generic_1").formatted(Formatting.DARK_PURPLE),
		Text.translatable("wwizardry.soul_mirror.generic_2").formatted(Formatting.DARK_PURPLE),
		Text.empty(),
		Text.translatable("wwizardry.soul_mirror.broken").formatted(Formatting.DARK_PURPLE)
	);

	public static void addTooltips(ItemStack stack, TooltipContext context, List<Text> lines) {
		if (stack.isOf(VoidBagItem.INSTANCE)) {
			var player = MinecraftClient.getInstance().player;
			if (player == null)
				return;
			var bag = VoidBagComponent.getForPlayer(player);
			lines.addAll(
				1,
				bag.locked ? VOID_BAG_LOCKED : VOID_BAG_UNLOCKED
			);
			return;
		}
		if (stack.isOf(SoulMirrorItem.INSTANCE)) {
			lines.addAll(
				1,
				SoulMirrorItem.INSTANCE.isFullyUsed(stack) ? SOUL_MIRROR_BROKEN : SOUL_MIRROR
			);
		}
	}
}
