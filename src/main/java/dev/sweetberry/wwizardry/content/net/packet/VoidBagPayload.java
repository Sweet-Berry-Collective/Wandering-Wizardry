package dev.sweetberry.wwizardry.content.net.packet;

import dev.sweetberry.wwizardry.Mod;
import dev.sweetberry.wwizardry.compat.cardinal.component.VoidBagComponent;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class VoidBagPayload {
	public static final ResourceLocation ID = Mod.id("void_bag");

	public static void accept(
		MinecraftServer server,
		ServerPlayer player,
		ServerGamePacketListenerImpl handler,
		FriendlyByteBuf buf,
		PacketSender responseSender
	) {
		player.server.execute(() -> {
			var bag = VoidBagComponent.getForPlayer(player);
			bag.openScreen();
		});
	}
}
