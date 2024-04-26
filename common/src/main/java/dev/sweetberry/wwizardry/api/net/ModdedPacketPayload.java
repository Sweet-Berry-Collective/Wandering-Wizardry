package dev.sweetberry.wwizardry.api.net;

import dev.sweetberry.wwizardry.WanderingWizardry;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public class ModdedPacketPayload implements CustomPacketPayload {
	public static final Type<ModdedPacketPayload> TYPE = new Type<>(WanderingWizardry.id("packet"));

	@Override @NotNull
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
