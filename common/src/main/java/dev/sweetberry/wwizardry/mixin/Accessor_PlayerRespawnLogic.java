package dev.sweetberry.wwizardry.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.PlayerRespawnLogic;
import net.minecraft.server.level.ServerLevel;
import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PlayerRespawnLogic.class)
public interface Accessor_PlayerRespawnLogic {
	@Invoker("getOverworldRespawnPos")
	static BlockPos invokeGetOverworldRespawnPos(ServerLevel level, int i, int j) {
		throw new NotImplementedException();
	}
}
