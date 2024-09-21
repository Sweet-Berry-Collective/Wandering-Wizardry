package dev.sweetberry.wwizardry.fabric.component;

import com.mojang.serialization.Codec;
import dev.sweetberry.wwizardry.api.component.Component;
import dev.sweetberry.wwizardry.content.component.BoatComponent;
import dev.sweetberry.wwizardry.content.component.ComponentInitializer;
import dev.sweetberry.wwizardry.content.component.VoidBagComponent;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class FabricComponents {
	private static final Map<ResourceLocation, AttachmentType<? super Component>> ATTACHMENTS = new HashMap<>();
	private static final Map<ResourceLocation, Supplier<? super Component>> INIT = new HashMap<>();
	public static final AttachmentType<BoatComponent> BOAT = register(ComponentInitializer.BOAT, BoatComponent.CODEC, BoatComponent::new);
	public static final AttachmentType<VoidBagComponent> VOID_BAG = register(ComponentInitializer.VOID_BAG, VoidBagComponent.CODEC, VoidBagComponent::new);

	public static void init() {}

	public static <T extends Component<T>> AttachmentType<T> register(ResourceLocation id, Codec<T> codec, Supplier<T> init) {
		var type = AttachmentRegistry.createPersistent(id, codec);
		ATTACHMENTS.put(id, (AttachmentType<? super Component>) (Object) type);
		INIT.put(id, (Supplier<? super Component>) (Object) init);
		return type;
	}

	public static <T extends Component<T>> T getComponent(ResourceLocation id, Entity entity) {
		var type = (AttachmentType<T>) (Object) ATTACHMENTS.get(id);
		var init = (Supplier<T>) (Object) INIT.get(id);
		return entity.getAttachedOrCreate(type, init);
	}
}
