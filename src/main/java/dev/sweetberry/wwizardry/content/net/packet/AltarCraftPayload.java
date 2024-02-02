package dev.sweetberry.wwizardry.content.net.packet;

import dev.sweetberry.wwizardry.Mod;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class AltarCraftPayload {
	public static final ResourceLocation ID = Mod.id("altar_craft");
	public BlockPos pos;
	public boolean bloom;

	public AltarCraftPayload(FriendlyByteBuf buf) {
		this(buf.readBlockPos(), buf.readBoolean());
	}
	public AltarCraftPayload(BlockPos pos, boolean bloom) {
		this.pos = pos;
		this.bloom = bloom;
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeBlockPos(pos);
		buf.writeBoolean(bloom);
	}
}
