package dev.sweetberry.wwizardry.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ServerPlayerEntity.class)
public interface Accessor_ServerPlayerEntity {
	@Invoker
	int invokeCalculateSpawnOffsetMultiplier(int horizontalSpawnArea);
}
