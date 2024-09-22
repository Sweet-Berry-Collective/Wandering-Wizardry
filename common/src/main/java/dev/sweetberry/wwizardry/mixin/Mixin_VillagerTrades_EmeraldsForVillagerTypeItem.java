package dev.sweetberry.wwizardry.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.sweetberry.wwizardry.content.datagen.DatagenInitializer;
import dev.sweetberry.wwizardry.content.villager.VillagerInitializer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerDataHolder;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Mixin(targets = {"net/minecraft/world/entity/npc/VillagerTrades$EmeraldsForVillagerTypeItem"})
public class Mixin_VillagerTrades_EmeraldsForVillagerTypeItem {
	@Shadow
	@Final
	private int cost;

	@Shadow
	@Final
	private int maxUses;

	@Shadow
	@Final
	private int villagerXp;

	@Shadow
	@Final
	private Map<VillagerType, Item> trades;

	@WrapOperation(
		method = "<init>",
		at = @At(
			value = "INVOKE",
			target = "Ljava/util/stream/Stream;filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;"
		)
	)
	private Stream<VillagerType> wrapFilter(Stream<VillagerType> instance, Predicate<? super VillagerType> predicate, Operation<Stream<VillagerType>> original) {
		return original.call(instance, (Predicate<? super VillagerType>)(it) -> it != VillagerInitializer.FUNGAL_FOREST_VILLAGER.get() && predicate.test(it));
	}

	@Inject(
		method = "getOffer",
		at = @At("HEAD"),
		cancellable = true
	)
	private void addWwizardryTrades(Entity entity, RandomSource random, CallbackInfoReturnable<MerchantOffer> cir) {
		if (entity instanceof VillagerDataHolder villager && villager.getVariant() == VillagerInitializer.FUNGAL_FOREST_VILLAGER.get()) {
			if (villager.getVillagerData().getProfession() == VillagerProfession.FISHERMAN) {
				assert DatagenInitializer.DENIA_WOOD.BOAT_ITEM != null;
				cir.setReturnValue(new MerchantOffer(new ItemCost(DatagenInitializer.DENIA_WOOD.BOAT_ITEM.get(), cost), new ItemStack(Items.EMERALD), maxUses, villagerXp, 0.05F));
			}

			// Fall back to plains
			ItemCost itemCost = new ItemCost(trades.get(VillagerType.PLAINS), cost);
			cir.setReturnValue(new MerchantOffer(itemCost, new ItemStack(Items.EMERALD), maxUses, villagerXp, 0.05F));
		}
	}
}
