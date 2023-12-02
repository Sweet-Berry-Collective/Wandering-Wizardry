package dev.sweetberry.wwizardry.mixin;

import com.mojang.authlib.GameProfile;
import dev.sweetberry.wwizardry.Badges;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.quiltmc.loader.api.QuiltLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
	value = PlayerEntity.class,
	priority = 2000
)
public abstract class Mixin_PlayerEntity {
	@Shadow
	public abstract GameProfile getGameProfile();

	@Inject(
		method = "getDisplayName",
		at = @At("RETURN")
	)
	private void wwizardry$getBadge(CallbackInfoReturnable<Text> cir) {
		// TODO: don't hardcode
		if (QuiltLoader.isModLoaded("styled-nicknames") || QuiltLoader.isModLoaded("styledchat"))
			return;
		if (!(cir.getReturnValue() instanceof MutableText mutableText))
			return;
		var badge = Badges.getBadgeFor(getGameProfile().getId());
		if (badge == null)
			return;
		mutableText.append(" ").append(badge);
	}
}
