package dev.sweetberry.wwizardry.content.net.packet;

import dev.sweetberry.wwizardry.Mod;
import dev.sweetberry.wwizardry.compat.cardinal.component.VoidBagComponent;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.payload.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class VoidBagPayload implements CustomPayload, FabricPacket {
	public static final Identifier ID = Mod.id("void_bag");
	public static final PacketType<VoidBagPayload> TYPE = PacketType.create(VoidBagPayload.ID, VoidBagPayload::new);

	public VoidBagPayload(PacketByteBuf buf) {}
	public VoidBagPayload() {}

	@Override
	public void write(PacketByteBuf buf) {}

	@Override
	public Identifier id() {
		return ID;
	}

	@Override
	public PacketType<?> getType() {
		return TYPE;
	}

	public static void accept(
		VoidBagPayload payload,
		ServerPlayerEntity player,
		PacketSender responseSender
	) {
		player.server.execute(() -> {
			var bag = VoidBagComponent.getForPlayer(player);
			bag.openScreen();
		});
	}
}
