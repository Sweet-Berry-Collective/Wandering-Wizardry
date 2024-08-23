package dev.sweetberry.wwizardry.content.item.charm;

import dev.sweetberry.wwizardry.api.altar.AltarRecipeView;
import dev.sweetberry.wwizardry.config.Config;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.ItemEnchantments;
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
		var bookEnchants = new ItemEnchantments.Mutable(EnchantmentHelper.getEnchantmentsForCrafting(book));

		for (var i : bookDirs) {
			for (var enchant : EnchantmentHelper.getEnchantmentsForCrafting(view.getItemInPedestal(i)).entrySet()) {
				if (!EnchantmentHelper.isEnchantmentCompatible(bookEnchants.keySet(), enchant.getKey()) && !Config.getAllowOpEnchants())
					return false;

				if (bookEnchants.getLevel(enchant.getKey()) > enchant.getIntValue())
					bookEnchants.upgrade(enchant.getKey(), enchant.getIntValue());
				else
					bookEnchants.set(enchant.getKey(), enchant.getIntValue());
			}
		}

		view.setRecipeResult(book);
		return true;
	}
}
