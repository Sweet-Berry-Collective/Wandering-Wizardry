package dev.sweetberry.wwizardry.api.net;

import com.mojang.serialization.Codec;
import dev.sweetberry.wwizardry.api.event.Event;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class PacketRegistry {
	private static final Map<CustomPacketPayload.Type<CustomPacket>, StreamCodec<FriendlyByteBuf, CustomPacket>> REGISTRY = new ConcurrentHashMap<>();
	private static final Event<BiConsumer<CustomPacketPayload.Type<CustomPacket>, StreamCodec<FriendlyByteBuf, CustomPacket>>> EVENT = new Event<>(listeners ->
		(id, codec) ->
			listeners.forEach(l ->
				l.accept(id, codec)
			)
	);

	public static final Event<BiConsumer<ServerPlayer, CustomPacket>> SEND_TO_CLIENT = new Event<>(listeners ->
		(player, packet) ->
			listeners.forEach(l ->
				l.accept(player, packet)
			)
	);

	public static final Event<Consumer<CustomPacket>> SEND_TO_SERVER = new Event<>(listeners ->
		(packet) ->
			listeners.forEach(l ->
				l.accept(packet)
			)
	);

	public static <T extends CustomPacket> void register(CustomPacketPayload.Type<T> type, StreamCodec<FriendlyByteBuf, T> codec) {
		REGISTRY.put((CustomPacketPayload.Type<CustomPacket>) type, (StreamCodec<FriendlyByteBuf, CustomPacket>) codec);
		EVENT.invoker().accept((CustomPacketPayload.Type<CustomPacket>) type, (StreamCodec<FriendlyByteBuf, CustomPacket>) codec);
	}

	public static void registerTo(BiConsumer<CustomPacketPayload.Type<CustomPacket>, StreamCodec<FriendlyByteBuf, CustomPacket>> callback) {
		REGISTRY.forEach(callback);
		EVENT.listen(callback);
	}

	public static <T extends CustomPacket> void sendToClient(ServerPlayer player, T packet) {
		SEND_TO_CLIENT.invoker().accept(player, packet);
	}

	public static <T extends CustomPacket> void sendToServer(T packet) {
		SEND_TO_SERVER.invoker().accept(packet);
	}
}
