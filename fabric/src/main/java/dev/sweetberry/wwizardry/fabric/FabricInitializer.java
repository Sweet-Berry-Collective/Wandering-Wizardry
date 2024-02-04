package dev.sweetberry.wwizardry.fabric;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.api.component.Component;
import dev.sweetberry.wwizardry.api.net.PacketRegistry;
import dev.sweetberry.wwizardry.compat.cardinal.CardinalInitializer;
import dev.sweetberry.wwizardry.compat.cardinal.component.ProxyComponent;
import dev.sweetberry.wwizardry.content.ContentInitializer;
import dev.sweetberry.wwizardry.content.component.ComponentInitializer;
import dev.sweetberry.wwizardry.content.item.ItemInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.CreativeModeTab;

public class FabricInitializer implements ModInitializer {
	public static final CreativeModeTab GROUP = ItemInitializer.registerTab(
		"items",
		FabricItemGroup.builder()
			.icon(ItemInitializer.CRYSTALLINE_SCULK_SHARD::getDefaultInstance)
			.displayItems((display, collector) -> collector.acceptAll(ItemInitializer.STACKS))
			.title(net.minecraft.network.chat.Component.translatable("itemGroup.wwizardry.items"))
			.build()
	);

    @Override
    public void onInitialize() {
		WanderingWizardry.init();
		ContentInitializer.init();
		ContentInitializer.listenToAll(Registry::register);

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

		ComponentInitializer.getter = FabricInitializer::getComponent;
    }

	public static <T extends Component> T getComponent(ResourceLocation id, Entity entity) {
		return ((ProxyComponent<T>)entity.getComponent(CardinalInitializer.COMPONENTS.get(id))).baseComponent;
	}
}
