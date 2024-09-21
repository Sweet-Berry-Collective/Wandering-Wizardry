package dev.sweetberry.wwizardry.fabric;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.api.net.PacketRegistry;
import dev.sweetberry.wwizardry.content.ContentInitializer;
import dev.sweetberry.wwizardry.content.block.BlockInitializer;
import dev.sweetberry.wwizardry.content.component.ComponentInitializer;
import dev.sweetberry.wwizardry.content.events.UseBlockHandler;
import dev.sweetberry.wwizardry.content.villager.VillagerInitializer;
import dev.sweetberry.wwizardry.content.world.WorldgenInitializer;
import dev.sweetberry.wwizardry.fabric.component.FabricComponents;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;

import java.util.List;

public class FabricInitializer implements ModInitializer {
    @Override
    public void onInitialize() {
		FabricComponents.init();

		ComponentInitializer.getter = FabricComponents::getComponent;
		WanderingWizardry.modLoadedCheck = FabricLoader.getInstance()::isModLoaded;

		ContentInitializer.listenToAll(((registry, id, item) -> {
			Registry.register(registry, id, item.get());
		}));

		PacketRegistry.SEND_TO_CLIENT.listen(ServerPlayNetworking::send);

		PacketRegistry.registerTo((id, codec) -> {
			PayloadTypeRegistry.playC2S().register(id, codec);
			ServerPlayNetworking.registerGlobalReceiver(id, (payload, context) -> {
				payload.onServerReceive(context.player().server, context.player().serverLevel(), context.player());
			});
		});

		UseBlockCallback.EVENT.register((player, world, hand, hitResult) ->
			UseBlockHandler.onBlockUse(player, world, hand, hitResult.getBlockPos(), hitResult.getDirection())
		);

		var modification = BiomeModifications.create(WanderingWizardry.id("modifications"));

		modification.add(ModificationPhase.ADDITIONS, BiomeSelectors.foundInOverworld(), (ctx, modifications) -> {
			var generationSettings = modifications.getGenerationSettings();
			for (var decoration : WorldgenInitializer.OVERWORLD_MODIFICATIONS.entrySet()) {
				var step = decoration.getKey();
				var features = decoration.getValue();
				for (var feature : features)
					generationSettings.addFeature(step, feature);
			}
		});

		FabricInitializer.addWanderingTradesFor(1);
		FabricInitializer.addWanderingTradesFor(2);

		WanderingWizardry.init("fabric");

		BlockInitializer.registerSecondaryBlockFunctions();

		for (var waxable : BlockInitializer.WAXABLES)
			OxidizableBlocksRegistry.registerWaxableBlockPair(waxable.getFirst().get(), waxable.getSecond().get());
		for (var waxable : BlockInitializer.WEATHERABLES)
			OxidizableBlocksRegistry.registerOxidizableBlockPair(waxable.getFirst().get(), waxable.getSecond().get());

		VillagerInitializer.addToBiomes();
    }

	private static void addWanderingTradesFor(int level) {
		TradeOfferHelper.registerWanderingTraderOffers(
			level,
			offers -> offers.addAll(List.of(VillagerInitializer.WANDERING_TRADER_OFFERS[level-1]))
		);
	}
}
