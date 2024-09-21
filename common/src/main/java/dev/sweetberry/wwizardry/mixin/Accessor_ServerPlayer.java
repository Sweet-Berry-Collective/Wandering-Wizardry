package dev.sweetberry.wwizardry.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Optional;

@Mixin(ServerPlayer.class)
public interface Accessor_ServerPlayer {
	@Invoker
	int invokeGetCoprime(int horizontalSpawnArea);

	@Invoker
	static Optional<ServerPlayer.RespawnPosAngle> invokeFindRespawnAndUseSpawnBlock(ServerLevel $$0, BlockPos $$1, float $$2, boolean $$3, boolean $$4) {
		throw new IllegalCallerException("Waaaa");
	}
}
