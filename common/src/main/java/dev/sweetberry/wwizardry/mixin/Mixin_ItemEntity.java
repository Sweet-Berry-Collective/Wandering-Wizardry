package dev.sweetberry.wwizardry.mixin;

import dev.sweetberry.wwizardry.content.component.ComponentInitializer;
import dev.sweetberry.wwizardry.content.component.VoidBagComponent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.UUID;
import net.minecraft.network.protocol.game.ClientboundTakeItemEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@Mixin(ItemEntity.class)
public abstract class Mixin_ItemEntity {
	@Shadow
	private int pickupDelay;

	@Shadow
	private @Nullable UUID thrower;

	@ModifyVariable(
		method = "playerTouch",
		at = @At("STORE"),
		ordinal = 0
	)
	private ItemStack wwizardry$insertVoidBag(ItemStack original, Player player) {
		var bag = ComponentInitializer.<VoidBagComponent>getComponent(ComponentInitializer.VOID_BAG, player);
		var self = (ItemEntity) (Object) this;
		if (
			pickupDelay != 0 ||
			(thrower != null && !thrower.equals(player.getUUID())) ||
			bag.locked ||
			!bag.contains(original.getItem())
		)
			return original;

		if (bag.tryAddStack(original) == 0)
			((ServerLevel) self.level()).getChunkSource().broadcast(self, new ClientboundTakeItemEntityPacket(self.getId(), player.getId(), 0));

		return original;
	}
}
