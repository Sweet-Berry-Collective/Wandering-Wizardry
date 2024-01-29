package dev.sweetberry.wwizardry.content.net.packet;

import dev.sweetberry.wwizardry.Mod;
import dev.sweetberry.wwizardry.compat.component.VoidBagComponent;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.payload.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.quiltmc.qsl.networking.api.PacketSender;

public class VoidBagPayload implements CustomPayload {
	public static final Identifier ID = Mod.id("void_bag");

	public VoidBagPayload(PacketByteBuf buf) {}
	public VoidBagPayload() {}

	@Override
	public void write(PacketByteBuf buf) {}

	@Override
	public Identifier id() {
		return ID;
	}

	public static void accept(
		MinecraftServer server,
		ServerPlayerEntity player,
		ServerPlayNetworkHandler handler,
		VoidBagPayload payload,
		PacketSender<CustomPayload> responseSender
	) {
		server.execute(() -> {
			var bag = VoidBagComponent.getForPlayer(player);
			bag.openScreen();
		});
	}
}
