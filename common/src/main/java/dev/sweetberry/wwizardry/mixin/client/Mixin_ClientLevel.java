package dev.sweetberry.wwizardry.mixin.client;

import dev.sweetberry.wwizardry.api.net.PacketRegistry;
import dev.sweetberry.wwizardry.content.component.ComponentInitializer;
import dev.sweetberry.wwizardry.content.net.packet.ComponentSyncPacket;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.Boat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public class Mixin_ClientLevel {
	@Inject(
		at = @At("RETURN"),
		method = "addEntity"
	)
	private void askForComponent(Entity entity, CallbackInfo ci) {
		if (!(entity instanceof Boat))
			return;
		PacketRegistry.sendToServer(new ComponentSyncPacket(ComponentInitializer.BOAT, entity.getId(), new CompoundTag()));
	}
}
