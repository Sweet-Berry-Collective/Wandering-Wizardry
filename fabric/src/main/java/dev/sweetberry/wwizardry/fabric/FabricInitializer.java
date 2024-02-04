package dev.sweetberry.wwizardry.fabric;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.api.net.PacketRegistry;
import dev.sweetberry.wwizardry.content.ContentInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Registry;

public class FabricInitializer implements ModInitializer {
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
    }
}
