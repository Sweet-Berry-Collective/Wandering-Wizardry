package dev.sweetberry.wwizardry.mixin;

import dev.sweetberry.wwizardry.content.component.BoatComponent;
import dev.sweetberry.wwizardry.content.component.ComponentInitializer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Boat.class)
public class Mixin_Boat {
	@Inject(
		method = "getTypeName",
		at = @At("HEAD"),
		cancellable = true
	)
	private void wwizardry$changeName(CallbackInfoReturnable<Component> cir) {
		var self = (Boat)(Object)this;
		var type = ComponentInitializer.<BoatComponent>getComponent(ComponentInitializer.BOAT, self).type;
		if (type == null)
			return;
		var chest = self instanceof ChestBoat;
		cir.setReturnValue(Component.translatable(
			"boat." + type.getNamespace() + "." + type.getPath() + (chest ? "_chest" : "")
		));
	}
}
