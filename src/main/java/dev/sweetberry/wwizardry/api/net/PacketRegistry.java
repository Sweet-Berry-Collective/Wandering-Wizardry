package dev.sweetberry.wwizardry.api.net;

import dev.sweetberry.wwizardry.api.event.Event;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class PacketRegistry {
	private static final Map<ResourceLocation, PacketConstructor<?>> REGISTRY = new ConcurrentHashMap<>();
	private static final Event<BiConsumer<ResourceLocation, PacketConstructor<?>>> EVENT = new Event<>(listeners ->
		(id, constructor) ->
			listeners.forEach(l ->
				l.accept(id, constructor)
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

	public static <T extends CustomPacket> void register(ResourceLocation id, PacketConstructor<T> packet) {
		REGISTRY.put(id, packet);
		EVENT.invoker().accept(id, packet);
	}

	public static void registerTo(BiConsumer<ResourceLocation, PacketConstructor<?>> callback) {
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
