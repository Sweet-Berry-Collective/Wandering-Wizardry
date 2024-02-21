package dev.sweetberry.wwizardry.fabric;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.api.Lazy;
import dev.sweetberry.wwizardry.api.component.Component;
import dev.sweetberry.wwizardry.api.net.PacketRegistry;
import dev.sweetberry.wwizardry.fabric.compat.cardinal.CardinalInitializer;
import dev.sweetberry.wwizardry.fabric.compat.cardinal.component.ProxyComponent;
import dev.sweetberry.wwizardry.content.ContentInitializer;
import dev.sweetberry.wwizardry.content.component.ComponentInitializer;
import dev.sweetberry.wwizardry.content.datagen.DatagenInitializer;
import dev.sweetberry.wwizardry.content.events.UseBlockHandler;
import dev.sweetberry.wwizardry.content.item.ItemInitializer;
import dev.sweetberry.wwizardry.content.trades.TradeInitializer;
import dev.sweetberry.wwizardry.content.world.WorldgenInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;

import java.util.List;
import java.util.stream.Collectors;

public class FabricInitializer implements ModInitializer {
    @Override
    public void onInitialize() {
		ComponentInitializer.getter = FabricInitializer::getComponent;
		WanderingWizardry.modLoadedCheck = FabricLoader.getInstance()::isModLoaded;

		ContentInitializer.listenToAll(((registry, id, item) -> {
			Registry.register(registry, id, item.get());
		}));

		PacketRegistry.SEND_TO_CLIENT.listen((player, packet) -> {
			var payload = PacketByteBufs.create();
			packet.writeTo(payload);
			ServerPlayNetworking.send(player, packet.getId(), payload);
		});

		PacketRegistry.registerTo((id, constructor) -> {
			ServerPlayNetworking.registerGlobalReceiver(id, ((server, player, handler, buf, responseSender) -> {
				var packet = constructor.create(buf);
				packet.onServerReceive(server, player.serverLevel(), player);
			}));
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

		if (WanderingWizardry.isModLoaded("quilt_resource_loader"))
			initWithQsl();

		FabricInitializer.addWanderingTradesFor(1);
		FabricInitializer.addWanderingTradesFor(2);

		WanderingWizardry.init("fabric");
    }

	public static void initWithQsl() {
		ResourceLoader.get(PackType.CLIENT_RESOURCES).getRegisterDefaultPackEvent().register(ctx -> {
			DatagenInitializer.reloadPack(ctx.resourceManager());
			ctx.addResourcePack(DatagenInitializer.pack);
		});
	}

	private static void addWanderingTradesFor(int level) {
		TradeOfferHelper.registerWanderingTraderOffers(
			level,
			offers -> offers.addAll(List.of(TradeInitializer.WANDERING_TRADER_OFFERS[level-1]))
		);
	}

	public static <T extends Component> T getComponent(ResourceLocation id, Entity entity) {
		return (T) entity.getComponent(CardinalInitializer.COMPONENTS.get(id)).baseComponent;
	}
}
