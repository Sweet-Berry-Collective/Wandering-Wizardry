package dev.sweetberry.wwizardry.content.net.packet;

import com.mojang.datafixers.util.Pair;
import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.api.component.Component;
import dev.sweetberry.wwizardry.api.net.CustomPacket;
import dev.sweetberry.wwizardry.api.net.PacketRegistry;
import dev.sweetberry.wwizardry.content.component.BoatComponent;
import dev.sweetberry.wwizardry.content.component.ComponentInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class ComponentSyncPacket implements CustomPacket {
	public static final ResourceLocation ID = WanderingWizardry.id("component_sync");
	public static final Type<ComponentSyncPacket> TYPE = new Type<>(ID);
	public static final StreamCodec<FriendlyByteBuf, ComponentSyncPacket> CODEC = StreamCodec.of(
		ComponentSyncPacket::writeTo, ComponentSyncPacket::readFrom
	);
	public ResourceLocation id;
	public int entity;
	public Tag component;

	public ComponentSyncPacket(ResourceLocation id, int entity, Tag component) {
		this.id = id;
		this.entity = entity;
		this.component = component;
	}

	public static void writeTo(FriendlyByteBuf buf, ComponentSyncPacket packet) {
		buf.writeResourceLocation(packet.id);
		buf.writeInt(packet.entity);
		buf.writeNbt(packet.component);
	}

	public static ComponentSyncPacket readFrom(FriendlyByteBuf buf) {
		var id = buf.readResourceLocation();
		var entity = buf.readInt();
		var component = buf.readNbt();
		return new ComponentSyncPacket(id, entity, component);
	}

	@Override
	public void onClientReceive(Minecraft client, ClientLevel world, AbstractClientPlayer receiver) {
		onClientReceiveGeneric(client, world, receiver);
	}

	private <T extends Component<T>> void onClientReceiveGeneric(Minecraft client, ClientLevel world, AbstractClientPlayer receiver) {
		// please java
		var component = ComponentInitializer.<T>getComponent(id, world.getEntity(entity));
		var codec = component.codec();
		codec.decode(NbtOps.INSTANCE, this.component).result().map(Pair::getFirst).ifPresent(component::copyFrom);
	}

	@Override
	public void onServerReceive(MinecraftServer server, ServerLevel world, ServerPlayer sender) {
		onServerReceiveGeneric(server, world, sender);
	}

	private <T extends Component<T>> void onServerReceiveGeneric(MinecraftServer server, ServerLevel world, ServerPlayer sender) {
		var component = ComponentInitializer.<T>getComponent(id, world.getEntity(entity));
		PacketRegistry.sendToClient(sender, new ComponentSyncPacket(id, entity, component.codec().encode(component, NbtOps.INSTANCE, this.component).result().orElse(this.component)));
	}

	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
