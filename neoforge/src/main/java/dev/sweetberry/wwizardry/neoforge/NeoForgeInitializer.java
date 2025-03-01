package dev.sweetberry.wwizardry.neoforge;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.client.WanderingWizardryClient;
import dev.sweetberry.wwizardry.client.content.events.ClientEvents;
import dev.sweetberry.wwizardry.client.content.events.PackReloader;
import dev.sweetberry.wwizardry.compat.terrablender.TerraBlenderInitializer;
import dev.sweetberry.wwizardry.content.ContentInitializer;
import dev.sweetberry.wwizardry.content.block.BlockInitializer;
import dev.sweetberry.wwizardry.content.entity.EntityInitializer;
import dev.sweetberry.wwizardry.content.villager.VillagerInitializer;
import dev.sweetberry.wwizardry.neoforge.component.NeoForgeComponents;
import dev.sweetberry.wwizardry.neoforge.networking.NeoForgeNetworking;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod("wwizardry")
public class NeoForgeInitializer {
	public NeoForgeInitializer(IEventBus bus, Dist dist) {
		bus.addListener(this::registerToRegistries);
		bus.addListener(this::commonSetup);
		bus.addListener(this::createEntityAttributes);
		WanderingWizardry.modLoadedCheck = ModList.get()::isLoaded;
		NeoForgeEvents.init();
		NeoForgeComponents.init(bus);
		NeoForgeNetworking.init(bus);
		WanderingWizardry.init("neoforge");

		if (dist == Dist.CLIENT)
			NeoForgeClientEvents.init(bus);
	}

	private void commonSetup(final FMLCommonSetupEvent event) {
		event.enqueueWork(TerraBlenderInitializer::init);
	}

	@SubscribeEvent
	public void createEntityAttributes(EntityAttributeCreationEvent event) {
		EntityInitializer.SUPPLIER_DATA.forEach(it -> {
			event.put((EntityType<? extends LivingEntity>) it.entity().get(), it.supplier().get());
		});
	}

	@SubscribeEvent
	public void registerToRegistries(RegisterEvent event) {
		ContentInitializer.listenToAll(((registry, id, item) -> {
			event.register(registry.key(), id, item);
		}));

		BlockInitializer.registerSecondaryBlockFunctions();
		VillagerInitializer.addToBiomes();
	}
}
