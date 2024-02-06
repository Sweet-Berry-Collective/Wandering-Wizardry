package dev.sweetberry.wwizardry.fabric.compat.cardinal.component;

import dev.onyxstudios.cca.api.v3.entity.PlayerComponent;
import dev.sweetberry.wwizardry.content.component.VoidBagComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class VoidBagCardinalComponent extends ProxyComponent<VoidBagComponent> implements PlayerComponent<VoidBagCardinalComponent> {
	public VoidBagCardinalComponent(Player player) {
		super(new VoidBagComponent());
	}

    @Override
	public boolean shouldCopyForRespawn(boolean lossless, boolean keepInventory, boolean sameCharacter) {
		return true;
	}
}
