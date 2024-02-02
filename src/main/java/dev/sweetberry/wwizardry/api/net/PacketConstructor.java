package dev.sweetberry.wwizardry.api.net;

import net.minecraft.network.FriendlyByteBuf;

@FunctionalInterface
public interface PacketConstructor<T extends CustomPacket> {
	T create(FriendlyByteBuf buf);
}
