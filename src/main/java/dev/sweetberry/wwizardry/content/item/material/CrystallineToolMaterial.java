package dev.sweetberry.wwizardry.content.item.material;

import dev.sweetberry.wwizardry.Mod;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public class CrystallineToolMaterial implements Tier {
	public static final CrystallineToolMaterial INSTANCE = new CrystallineToolMaterial();

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
		return Ingredient.of(TagKey.create(Registries.ITEM, Mod.id("repairs_sculk")));
	}
}
