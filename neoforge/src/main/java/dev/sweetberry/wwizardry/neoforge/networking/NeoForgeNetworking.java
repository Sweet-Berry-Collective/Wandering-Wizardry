package dev.sweetberry.wwizardry.neoforge.networking;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.api.net.CustomPacket;
import dev.sweetberry.wwizardry.api.net.PacketConstructor;
import dev.sweetberry.wwizardry.api.net.PacketRegistry;
import dev.sweetberry.wwizardry.content.net.NetworkingInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.handlers.ClientPayloadHandler;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

import java.util.HashMap;
import java.util.Map;

public class NeoForgeNetworking {

	public static void init(IEventBus bus) {
		bus.addListener(NeoForgeNetworking::register);
	}

	@SubscribeEvent
	public static void register(RegisterPayloadHandlerEvent event) {
		PacketRegistry.registerTo((id, constructor) -> {
			ModdedPacketPayload.PACKETS.put(id, constructor);
		});

		IPayloadRegistrar registrar = event.registrar(WanderingWizardry.MODID);
		registrar.play(
			ModdedPacketPayload.ID,
			ModdedPacketPayload::new,
			handler -> handler
				.client(ModdedPacketPayload::onClientReceive)
				.server(ModdedPacketPayload::onServerReceive)
		);

		registrar.play(
			ComponentSyncPayload.ID,
			ComponentSyncPayload::new,
			handler -> handler
				.client(ComponentSyncPayload::onClientReceive)
				.server(ComponentSyncPayload::onServerReceive)
		);

		PacketRegistry.SEND_TO_SERVER.listen(customPacket -> {
			PacketDistributor.SERVER.noArg().send(new ModdedPacketPayload(customPacket));
		});

		PacketRegistry.SEND_TO_CLIENT.listen((player, customPacket) -> {
			PacketDistributor.PLAYER.with(player).send(new ModdedPacketPayload(customPacket));
		});
	}
}
