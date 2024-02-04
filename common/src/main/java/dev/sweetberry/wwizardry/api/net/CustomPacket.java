package dev.sweetberry.wwizardry.api.net;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public interface CustomPacket {
	void writeTo(FriendlyByteBuf buf);

	void onClientReceive(Minecraft client, ClientLevel world, AbstractClientPlayer receiver);

	void onServerReceive(MinecraftServer server, ServerLevel world, ServerPlayer sender);

	ResourceLocation getId();
}
