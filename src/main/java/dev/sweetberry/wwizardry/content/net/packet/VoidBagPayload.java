package dev.sweetberry.wwizardry.content.net.packet;

import dev.sweetberry.wwizardry.Mod;
import dev.sweetberry.wwizardry.compat.cardinal.component.VoidBagComponent;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class VoidBagPayload implements CustomPacketPayload, FabricPacket {
	public static final ResourceLocation ID = Mod.id("void_bag");
	public static final PacketType<VoidBagPayload> TYPE = PacketType.create(VoidBagPayload.ID, VoidBagPayload::new);

	public VoidBagPayload(FriendlyByteBuf buf) {}
	public VoidBagPayload() {}

	@Override
	public void write(FriendlyByteBuf buf) {}

	@Override
	public ResourceLocation id() {
		return ID;
	}

	@Override
	public PacketType<?> getType() {
		return TYPE;
	}

	public static void accept(
		VoidBagPayload payload,
		ServerPlayer player,
		PacketSender responseSender
	) {
		player.server.execute(() -> {
			var bag = VoidBagComponent.getForPlayer(player);
			bag.openScreen();
		});
	}
}
