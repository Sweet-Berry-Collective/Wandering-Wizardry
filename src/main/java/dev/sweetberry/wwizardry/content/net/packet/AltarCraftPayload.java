package dev.sweetberry.wwizardry.content.net.packet;

import dev.sweetberry.wwizardry.Mod;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public class AltarCraftPayload implements CustomPacketPayload, FabricPacket {
	public static final ResourceLocation ID = Mod.id("altar_craft");
	public static final PacketType<AltarCraftPayload> TYPE = PacketType.create(AltarCraftPayload.ID, AltarCraftPayload::new);
	public BlockPos pos;
	public boolean bloom;

	public AltarCraftPayload(FriendlyByteBuf buf) {
		this(buf.readBlockPos(), buf.readBoolean());
	}
	public AltarCraftPayload(BlockPos pos, boolean bloom) {
		this.pos = pos;
		this.bloom = bloom;
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeBlockPos(pos);
		buf.writeBoolean(bloom);
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}

	@Override
	public PacketType<?> getType() {
		return TYPE;
	}
}
