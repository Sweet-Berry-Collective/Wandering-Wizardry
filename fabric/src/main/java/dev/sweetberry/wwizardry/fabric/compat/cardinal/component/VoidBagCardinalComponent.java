package dev.sweetberry.wwizardry.fabric.compat.cardinal.component;

import dev.sweetberry.wwizardry.content.component.VoidBagComponent;
import net.minecraft.world.entity.player.Player;
import org.ladysnake.cca.api.v3.entity.RespawnableComponent;

public class VoidBagCardinalComponent extends ProxyComponent<VoidBagComponent> implements RespawnableComponent<VoidBagCardinalComponent> {
	public VoidBagCardinalComponent(Player player) {
		super(new VoidBagComponent());
	}

	@Override
	public boolean shouldCopyForRespawn(boolean lossless, boolean keepInventory, boolean sameCharacter) {
		return true;
	}
}
