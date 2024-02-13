package dev.sweetberry.wwizardry.mixin;

import dev.sweetberry.wwizardry.content.item.SelfRemainderingItem;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class Mixin_Item {
	@Inject(
		method = "getCraftingRemainingItem",
		at = @At("RETURN"),
		cancellable = true
	)
	private void wwizardry$setRemainder(CallbackInfoReturnable<Item> cir) {
		var self = (Item)(Object)this;
		if (self instanceof SelfRemainderingItem)
			cir.setReturnValue(self);
	}
}
