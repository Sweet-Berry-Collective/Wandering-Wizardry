package dev.sweetberry.wwizardry.client.content.net;

import dev.sweetberry.wwizardry.content.net.packet.AltarCraftPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

public class ClientNetworkingInitializer {
	public static void init() {
		ClientPlayNetworking.registerGlobalReceiver(AltarCraftPayload.TYPE, ClientNetworkingInitializer::onAltarCraft);
	}

	static void onAltarCraft(AltarCraftPayload packet, LocalPlayer player, PacketSender responseSender) {
		var level = player.level();
		var worldPosition = packet.pos;

		level.addParticle(ParticleTypes.SONIC_BOOM, worldPosition.getX() + 0.5, worldPosition.getY() + 5.5, worldPosition.getZ() + 0.5, 0, 0, 0);
		level.playLocalSound(worldPosition.getX() + 0.5, worldPosition.getY() + 5.5, worldPosition.getZ() + 0.5, SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.BLOCKS, 1, 1, true);

		if (packet.bloom)
			level.playLocalSound(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, SoundEvents.SCULK_CATALYST_BLOOM, SoundSource.BLOCKS, 1, 1, true);
	}
}
