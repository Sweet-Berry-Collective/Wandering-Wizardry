package dev.sweetberry.wwizardry.neoforge.networking;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.api.component.Component;
import dev.sweetberry.wwizardry.content.component.ComponentInitializer;
import dev.sweetberry.wwizardry.neoforge.NeoForgeEvents;
import dev.sweetberry.wwizardry.neoforge.component.NeoForgeComponents;
import dev.sweetberry.wwizardry.neoforge.component.ProxyComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.Boat;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

import java.util.HashMap;
import java.util.Map;

public class ComponentSyncPayload implements CustomPacketPayload {
	public static final ResourceLocation ID = WanderingWizardry.id("sync");
	private int entityId;
	private ResourceLocation id;
	private CompoundTag component;

	public ComponentSyncPayload(Entity entity, ResourceLocation id, Component component) {
		entityId = entity.getId();
		this.id = id;
		var tag = new CompoundTag();
		component.toNbt(tag);
		this.component = tag;
	}

	public ComponentSyncPayload(FriendlyByteBuf buf) {
		entityId = buf.readInt();
		id = buf.readResourceLocation();
		component = buf.readNbt();
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeInt(entityId);
		buf.writeResourceLocation(id);
		buf.writeNbt(component);
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}

	public static void onClientReceive(ComponentSyncPayload payload, PlayPayloadContext context) {
		var entity = Minecraft.getInstance().level.getEntity(payload.entityId);
		if (entity == null)
			return;
		WanderingWizardry.LOGGER.info("client: " + payload.entityId + " " + entity);
		var component = ComponentInitializer.getComponent(payload.id, entity);
		component.fromNbt(payload.component);
	}

	public static void onServerReceive(ComponentSyncPayload payload, PlayPayloadContext context) {
		var entity = context.player().get().level().getEntity(payload.entityId);
		if (entity == null)
			return;
		WanderingWizardry.LOGGER.info("server: " + payload.entityId + " " + entity);
		var componenet = ComponentInitializer.getComponent(payload.id, entity);
		var out = new ComponentSyncPayload(entity, payload.id, componenet);
		PacketDistributor.PLAYER.with((ServerPlayer) context.player().get()).send(out);
	}
}
