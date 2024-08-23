package dev.sweetberry.wwizardry.fabric.mixin;

import dev.sweetberry.wwizardry.content.entity.EntityInitializer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DefaultAttributes.class)
public class Mixin_DefaultAttributes {
	@Inject(
		at = @At("RETURN"),
		method = "getSupplier",
		cancellable = true
	)
	private static void getModdedSupplier(EntityType<? extends LivingEntity> entity, CallbackInfoReturnable<AttributeSupplier> cir) {
		for (var data : EntityInitializer.SUPPLIER_DATA) {
			if (data.entity().get() == entity) {
				cir.setReturnValue(data.supplier().get());
				return;
			}
		}
	}

	@Inject(
		at = @At("RETURN"),
		method = "hasSupplier",
		cancellable = true
	)
	private static void hasModdedSupplier(EntityType<?> entity, CallbackInfoReturnable<Boolean> cir) {
		for (var data : EntityInitializer.SUPPLIER_DATA) {
			if (data.entity().get() == entity) {
				cir.setReturnValue(true);
				return;
			}
		}
	}
}
