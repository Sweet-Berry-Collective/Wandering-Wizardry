package dev.sweetberry.wwizardry.fabric.mixin;

import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PotionBrewing.Mix.class)
public interface Accessor_PotionBrewing_Mix<T> {
	@Accessor
	Ingredient getIngredient();

	@Accessor
	T getFrom();

	@Accessor
	T getTo();
}
