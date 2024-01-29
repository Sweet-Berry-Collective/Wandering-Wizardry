package dev.sweetberry.wwizardry.mixin;

import dev.sweetberry.wwizardry.compat.component.VoidBagComponent;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ItemPickupAnimationS2CPacket;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.UUID;

@Mixin(ItemEntity.class)
public abstract class Mixin_ItemEntity {
	@Shadow
	private int pickupDelay;

	@Shadow
	private @Nullable UUID owner;

	@ModifyVariable(
		method = "onPlayerCollision",
		at = @At("STORE"),
		ordinal = 0
	)
	private ItemStack wwizardry$insertVoidBag(ItemStack original, PlayerEntity player) {
		var bag = VoidBagComponent.getForPlayer(player);
		var self = (ItemEntity) (Object) this;
		if (
			pickupDelay != 0 ||
			(owner != null && !owner.equals(player.getUuid())) ||
			bag.locked ||
			!bag.contains(original.getItem())
		)
			return original;

		if (bag.tryAddStack(original) == 0)
			((ServerWorld) self.getWorld()).getChunkManager().sendToOtherNearbyPlayers(self, new ItemPickupAnimationS2CPacket(self.getId(), player.getId(), 0));

		return original;
	}
}
