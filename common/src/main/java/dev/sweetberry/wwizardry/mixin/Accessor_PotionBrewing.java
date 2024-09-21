package dev.sweetberry.wwizardry.mixin;

import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(PotionBrewing.class)
public interface Accessor_PotionBrewing {
	@Accessor("containers")
	List<Ingredient> getContainers();

	@Accessor("potionMixes")
	List<PotionBrewing.Mix<Potion>> getMixes();
}
