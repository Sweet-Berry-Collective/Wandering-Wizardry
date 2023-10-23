package dev.sweetberry.wwizardry.item.material;

import dev.sweetberry.wwizardry.WanderingMod;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class CrystallineSculkToolMaterial implements ToolMaterial {
	public static final CrystallineSculkToolMaterial INSTANCE = new CrystallineSculkToolMaterial();

	private CrystallineSculkToolMaterial() {}

	@Override
	public int getDurability() {
		return 129;
	}

	@Override
	public float getMiningSpeedMultiplier() {
		return 1;
	}

	@Override
	public float getAttackDamage() {
		return 0;
	}

	@Override
	public int getMiningLevel() {
		return 0;
	}

	@Override
	public int getEnchantability() {
		return 10;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return Ingredient.ofTag(TagKey.of(RegistryKeys.ITEM, WanderingMod.id("repairs_sculk")));
	}
}
