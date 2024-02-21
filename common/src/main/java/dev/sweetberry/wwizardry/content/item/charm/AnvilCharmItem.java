package dev.sweetberry.wwizardry.content.item.charm;

import dev.sweetberry.wwizardry.api.altar.AltarRecipeView;
import dev.sweetberry.wwizardry.config.Config;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;

import java.util.ArrayList;

public class AnvilCharmItem extends AltarCharmItem {
	public AnvilCharmItem(Properties settings) {
		super(settings);
	}

	@Override
	public boolean tryCraft(AltarRecipeView view, Level world) {
		final var enchantedBookItem = (EnchantedBookItem)Items.ENCHANTED_BOOK;

		view.keepCenter();
		var bookDirs = new ArrayList<AltarRecipeView.AltarDirection>();
		for (var i : AltarRecipeView.AltarDirection.cardinals()) {
			var item = view.getItemInPedestal(i);
			if (item == null)
				return false;
			if (item.is(enchantedBookItem))
				bookDirs.add(i);
			else
				view.setResultInPedestal(i, item);
		}
		if (bookDirs.isEmpty())
			return false;
		var book = enchantedBookItem.getDefaultInstance();

		for (var i : bookDirs) {
			for (var enchant : EnchantmentHelper.getEnchantments(view.getItemInPedestal(i)).entrySet()) {
				if (!EnchantmentHelper.isEnchantmentCompatible(EnchantmentHelper.getEnchantments(book).keySet(), enchant.getKey()) && !Config.getAllowOpEnchants())
					return false;

				EnchantedBookItem.addEnchantment(book, new EnchantmentInstance(enchant.getKey(), enchant.getValue()));
			}
		}

		view.setRecipeResult(book);
		return true;
	}
}
