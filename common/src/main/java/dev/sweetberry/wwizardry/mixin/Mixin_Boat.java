package dev.sweetberry.wwizardry.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.sweetberry.wwizardry.content.component.BoatComponent;
import dev.sweetberry.wwizardry.content.component.ComponentInitializer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
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

	@Inject(
		method = "getDropItem",
		at = @At("RETURN"),
		cancellable = true
	)
	private void wwizardry$getDropItem(CallbackInfoReturnable<Item> cir) {
		var self = (Boat)(Object)this;
		var type = ComponentInitializer.<BoatComponent>getComponent(ComponentInitializer.BOAT, self).type;
		if (type == null)
			return;
		var boat = BoatComponent.BOATS.get(type);
		var chest = self instanceof ChestBoat;
		cir.setReturnValue((chest ? boat.chest() : boat.boat()).get());
	}

	@WrapOperation(
		method = "checkFallDamage",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/vehicle/Boat$Type;getPlanks()Lnet/minecraft/world/level/block/Block;"
		)
	)
	private Block wwizardry$repacePlanks(Boat.Type instance, Operation<Block> original) {
		var self = (Boat)(Object)this;
		var type = ComponentInitializer.<BoatComponent>getComponent(ComponentInitializer.BOAT, self).type;
		if (type == null)
			return original.call(instance);
		var boat = BoatComponent.BOATS.get(type);
		return boat.planks().get();
	}
}
