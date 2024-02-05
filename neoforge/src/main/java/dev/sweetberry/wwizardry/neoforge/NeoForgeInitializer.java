package dev.sweetberry.wwizardry.neoforge;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.content.ContentInitializer;
import dev.sweetberry.wwizardry.content.trades.TradeInitializer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.apache.commons.lang3.NotImplementedException;

@Mod("wwizardry")
public class NeoForgeInitializer {
	public NeoForgeInitializer(IEventBus bus) {
		bus.addListener(this::register);
		TradeInitializer.addWanderingTradesFor = (i) -> {};
		WanderingWizardry.init();
	}

	@SubscribeEvent
	public void register(RegisterEvent event) {
		ContentInitializer.listenToAll(((registry, id, item) -> {
			event.register(registry.key(), (helper) -> {
				helper.register(id, item);
			});
		}));
	}
}
