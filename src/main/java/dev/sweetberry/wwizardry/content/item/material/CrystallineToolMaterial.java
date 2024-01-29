package dev.sweetberry.wwizardry.content.item.material;

import dev.sweetberry.wwizardry.Mod;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class CrystallineToolMaterial implements ToolMaterial {
	public static final CrystallineToolMaterial INSTANCE = new CrystallineToolMaterial();

	private CrystallineToolMaterial() {}

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
		return Ingredient.ofTag(TagKey.of(RegistryKeys.ITEM, Mod.id("repairs_sculk")));
	}
}
