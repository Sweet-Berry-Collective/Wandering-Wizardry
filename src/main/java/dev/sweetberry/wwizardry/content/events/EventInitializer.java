package dev.sweetberry.wwizardry.content.events;

import dev.sweetberry.wwizardry.api.altar.AltarCraftable;
import dev.sweetberry.wwizardry.api.registry.RegistryEntryWatcher;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.registry.Registries;

public class EventInitializer {
	public static void init() {
		UseBlockCallback.EVENT.register(UseBlockHandler::onBlockUse);
		AltarCraftable.EVENT.register(AltarCraftableHandler::tryCraft);
		new RegistryEntryWatcher<>(Registries.BLOCK).apply(RegistryMonitorHandler::onBlockAdded);
	}
}
