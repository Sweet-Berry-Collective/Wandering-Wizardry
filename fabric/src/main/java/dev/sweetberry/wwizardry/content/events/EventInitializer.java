package dev.sweetberry.wwizardry.content.events;

import dev.sweetberry.wwizardry.api.altar.AltarCraftable;
import dev.sweetberry.wwizardry.api.registry.RegistryEntryWatcher;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.registries.BuiltInRegistries;

public class EventInitializer {
	public static void init() {
		UseBlockCallback.EVENT.register(UseBlockHandler::onBlockUse);
		AltarCraftable.EVENT.listen(AltarCraftableHandler::tryCraft);
		new RegistryEntryWatcher<>(BuiltInRegistries.BLOCK).apply(RegistryMonitorHandler::onBlockAdded);
	}
}
