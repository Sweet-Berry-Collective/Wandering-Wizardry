package dev.sweetberry.wwizardry.mixin;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;
import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(PotionBrewing.class)
public interface Accessor_PotionBrewing {
	@Accessor("ALLOWED_CONTAINERS")
	static List<Ingredient> getAllowedContainers() {
		throw new NotImplementedException();
	}

	@Accessor("POTION_MIXES")
	static List<PotionBrewing.Mix<Potion>> getMixes() {
		throw new NotImplementedException();
	}
}
