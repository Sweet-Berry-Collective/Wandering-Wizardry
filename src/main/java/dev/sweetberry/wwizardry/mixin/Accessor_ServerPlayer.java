package dev.sweetberry.wwizardry.mixin;

import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ServerPlayer.class)
public interface Accessor_ServerPlayer {
	@Invoker
	int invokeGetCoprime(int horizontalSpawnArea);
}
