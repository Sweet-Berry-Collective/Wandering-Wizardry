package dev.sweetberry.wwizardry.content.events;

import dev.sweetberry.wwizardry.api.altar.AltarCraftable;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.registry.Registries;
import org.quiltmc.qsl.registry.api.event.RegistryMonitor;

public class EventInitializer {
	public static void init() {
		UseBlockCallback.EVENT.register(UseBlockHandler::onBlockUse);
		RegistryMonitor.create(Registries.BLOCK).forAll((ctx) -> RegistryMonitorHandler.onBlockAdded(ctx.value(), ctx.id()));
		AltarCraftable.EVENT.register(AltarCraftableHandler::tryCraft);
	}
}
