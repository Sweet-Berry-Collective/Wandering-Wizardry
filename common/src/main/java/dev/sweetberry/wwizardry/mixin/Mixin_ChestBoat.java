package dev.sweetberry.wwizardry.mixin;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.content.component.BoatComponent;
import dev.sweetberry.wwizardry.content.component.ComponentInitializer;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChestBoat.class)
public class Mixin_ChestBoat {
	@Inject(
		method = "getDropItem",
		at = @At("RETURN"),
		cancellable = true
	)
	private void wwizardry$getDropItem(CallbackInfoReturnable<Item> cir) {
		var self = (Boat)(Object)this;
		var type = ComponentInitializer.<BoatComponent>getComponent(ComponentInitializer.BOAT, self).type;
		WanderingWizardry.LOGGER.info("{}", type);
		if (type == null)
			return;
		var boat = BoatComponent.BOATS.get(type);
		cir.setReturnValue(boat.chest().get());
	}
}
