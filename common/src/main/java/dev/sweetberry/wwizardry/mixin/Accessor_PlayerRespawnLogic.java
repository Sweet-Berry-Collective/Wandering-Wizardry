package dev.sweetberry.wwizardry.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.PlayerRespawnLogic;
import net.minecraft.server.level.ServerLevel;
import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PlayerRespawnLogic.class)
public interface Accessor_PlayerRespawnLogic {
	@Invoker
	static BlockPos invokeGetOverworldRespawnPos(ServerLevel $$0, int $$1, int $$2) {
		throw new NotImplementedException();
	}
}
