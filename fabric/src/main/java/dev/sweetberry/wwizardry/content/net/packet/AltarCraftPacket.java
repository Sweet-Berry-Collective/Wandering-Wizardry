package dev.sweetberry.wwizardry.content.net.packet;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.api.net.CustomPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

public class AltarCraftPacket implements CustomPacket {
	public static final ResourceLocation ID = WanderingWizardry.id("altar_craft");
	public BlockPos pos;
	public boolean bloom;

	public AltarCraftPacket(FriendlyByteBuf buf) {
		this(buf.readBlockPos(), buf.readBoolean());
	}

	public AltarCraftPacket(BlockPos pos, boolean bloom) {
		this.pos = pos;
		this.bloom = bloom;
	}

	public void writeTo(FriendlyByteBuf buf) {
		buf.writeBlockPos(pos);
		buf.writeBoolean(bloom);
	}

	@Override
	public void onClientReceive(Minecraft client, ClientLevel world, AbstractClientPlayer receiver) {

		world.addParticle(ParticleTypes.SONIC_BOOM, pos.getX() + 0.5, pos.getY() + 5.5, pos.getZ() + 0.5, 0, 0, 0);
		world.playLocalSound(pos.getX() + 0.5, pos.getY() + 5.5, pos.getZ() + 0.5, SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.BLOCKS, 1, 1, true);

		if (bloom)
			world.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.SCULK_CATALYST_BLOOM, SoundSource.BLOCKS, 1, 1, true);
	}

	@Override
	public void onServerReceive(MinecraftServer server, ServerLevel world, ServerPlayer sender) {}

	@Override
	public ResourceLocation getId() {
		return ID;
	}
}
