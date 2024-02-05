package dev.sweetberry.wwizardry.mixin;

import com.mojang.authlib.GameProfile;
import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.api.Badges;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
	value = Player.class,
	priority = 2000
)
public abstract class Mixin_Player {
	@Shadow
	public abstract GameProfile getGameProfile();

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
}
