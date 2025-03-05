package dev.sweetberry.wwizardry.mixin;

import com.mojang.authlib.GameProfile;
import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.api.Badges;
import dev.sweetberry.wwizardry.content.item.ItemInitializer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
	value = Player.class,
	priority = 2000
)
public abstract class Mixin_Player {
	@Shadow
	public abstract GameProfile getGameProfile();

	@Shadow
	public abstract Inventory getInventory();

	@Inject(
		method = "getDisplayName",
		at = @At("RETURN")
	)
	private void wwizardry$getBadge(CallbackInfoReturnable<Component> cir) {
		// TODO: don't hardcode
		if (WanderingWizardry.isModLoaded("styled-nicknames") || WanderingWizardry.isModLoaded("styledchat"))
			return;
		if (!(cir.getReturnValue() instanceof MutableComponent mutableText))
			return;
		var badge = Badges.getBadgeFor(getGameProfile().getId());
		if (badge == null)
			return;
		mutableText.append(" ").append(badge);
	}

	@ModifyArg(
		method = "aiStep",
		at = @At(
			target = "Lnet/minecraft/world/level/Level;getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;",
			value = "INVOKE"
		),
		index = 1
	)
	AABB wwizardry$changePickupRange(AABB box) {
		if (getInventory().contains((it) -> it.is(ItemInitializer.ECHO_LURE.get())))
			return box.inflate(2);
		return box;
	}
}
