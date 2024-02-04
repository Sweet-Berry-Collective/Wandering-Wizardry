package dev.sweetberry.wwizardry.content.item.material;

import dev.sweetberry.wwizardry.WanderingWizardry;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public class CrystallineToolMaterial implements Tier {
	public static final CrystallineToolMaterial INSTANCE = new CrystallineToolMaterial();
	public static final TagKey<Item> REPAIRS_SCULK = TagKey.create(Registries.ITEM, WanderingWizardry.id("repairs_sculk"));

	private CrystallineToolMaterial() {}

	@Override
	public int getUses() {
		return 129;
	}

	@Override
	public float getSpeed() {
		return 1;
	}

	@Override
	public float getAttackDamageBonus() {
		return 0;
	}

	@Override
	public int getLevel() {
		return 0;
	}

	@Override
	public int getEnchantmentValue() {
		return 10;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return Ingredient.of(REPAIRS_SCULK);
	}
}
