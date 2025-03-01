package dev.sweetberry.wwizardry.fabric.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.sweetberry.wwizardry.content.villager.VillagerInitializer;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.VillagerType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Predicate;
import java.util.stream.Stream;

@Mixin(VillagerTrades.EmeraldsForVillagerTypeItem.class)
public class Mixin_VillagerTrades_EmeraldsForVillagerTypeItem {
	@WrapOperation(
		method = "<init>",
		at = @At(
			value = "INVOKE",
			target = "Ljava/util/stream/Stream;filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;"
		),
		expect = -1
	)
	private Stream<VillagerType> wrapFilter(Stream<VillagerType> instance, Predicate<? super VillagerType> predicate, Operation<Stream<VillagerType>> original) {
		return original.call(instance, (Predicate<? super VillagerType>)(it) -> it != VillagerInitializer.FUNGAL_FOREST_VILLAGER.get() && predicate.test(it));
	}
}
