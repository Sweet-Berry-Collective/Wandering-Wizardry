package dev.sweetberry.wwizardry.neoforge.networking;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.api.net.CustomPacket;
import dev.sweetberry.wwizardry.api.net.PacketConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

import java.util.HashMap;
import java.util.Map;

public class ModdedPacketPayload implements CustomPacketPayload {
	public static Map<ResourceLocation, PacketConstructor<?>> PACKETS = new HashMap<>();

	public static final ResourceLocation ID = WanderingWizardry.id("packet");

	public CustomPacket packet;

	public ModdedPacketPayload(FriendlyByteBuf buf) {
		var id = buf.readResourceLocation();
		packet = PACKETS.get(id).create(buf);
	}

	public ModdedPacketPayload(CustomPacket packet) {
		this.packet = packet;
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeResourceLocation(packet.getId());
		packet.writeTo(buf);
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}

	public static void onClientReceive(ModdedPacketPayload payload, PlayPayloadContext context) {
		var minecraft = Minecraft.getInstance();
		payload.packet.onClientReceive(Minecraft.getInstance(), minecraft.level, minecraft.player);
	}

	public static void onServerReceive(ModdedPacketPayload payload, PlayPayloadContext context) {
		var player = (ServerPlayer) context.player().get();
		payload.packet.onServerReceive(player.server, player.serverLevel(), player);
	}
}
