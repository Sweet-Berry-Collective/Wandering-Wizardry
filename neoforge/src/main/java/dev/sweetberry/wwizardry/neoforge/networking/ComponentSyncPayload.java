package dev.sweetberry.wwizardry.neoforge.networking;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.api.component.Component;
import dev.sweetberry.wwizardry.content.component.ComponentInitializer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.PacketDistributor;

public class ComponentSyncPayload implements CustomPacketPayload {
	public static final ResourceLocation ID = WanderingWizardry.id("sync");
	public static final Type<ComponentSyncPayload> TYPE = new Type<>(ID);
	public static final StreamCodec<FriendlyByteBuf, ComponentSyncPayload> CODEC = StreamCodec.of(
		ComponentSyncPayload::writeTo, ComponentSyncPayload::new
	);
	private int entityId;
	private ResourceLocation id;
	private CompoundTag component;

	public ComponentSyncPayload(Entity entity, ResourceLocation id, Component component) {
		entityId = entity.getId();
		this.id = id;
		var tag = new CompoundTag();
		component.toNbt(tag, entity.level().registryAccess());
		this.component = tag;
	}

	public ComponentSyncPayload(FriendlyByteBuf buf) {
		entityId = buf.readInt();
		id = buf.readResourceLocation();
		component = buf.readNbt();
	}

	public static void writeTo(FriendlyByteBuf buf, ComponentSyncPayload packet) {
		buf.writeInt(packet.entityId);
		buf.writeResourceLocation(packet.id);
		buf.writeNbt(packet.component);
	}

	public static void onClientReceive(ComponentSyncPayload payload, ClientLevel level) {
		var entity = level.getEntity(payload.entityId);
		if (entity == null)
			return;
		WanderingWizardry.LOGGER.info("client: " + payload.entityId + " " + entity);
		var component = ComponentInitializer.getComponent(payload.id, entity);
		component.fromNbt(payload.component, level.registryAccess());
	}

	public static void onServerReceive(ComponentSyncPayload payload, ServerPlayer player) {
		var entity = player.serverLevel().getEntity(payload.entityId);
		if (entity == null)
			return;
		WanderingWizardry.LOGGER.info("server: " + payload.entityId + " " + entity);
		var componenet = ComponentInitializer.getComponent(payload.id, entity);
		var out = new ComponentSyncPayload(entity, payload.id, componenet);
		PacketDistributor.sendToPlayer(player, out);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
