package dev.sweetberry.wwizardry.neoforge.component;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.api.component.Component;
import dev.sweetberry.wwizardry.content.component.BoatComponent;
import dev.sweetberry.wwizardry.content.component.ComponentInitializer;
import dev.sweetberry.wwizardry.content.component.VoidBagComponent;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class NeoForgeComponents {
	public static final Map<ResourceLocation, Supplier<AttachmentType<ProxyComponent<?>>>> COMPONENTS = new HashMap<>();
	private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, WanderingWizardry.MODID);

	private static final Supplier<AttachmentType<ProxyComponent<VoidBagComponent>>> VOID_BAG = create("void_bag", VoidBagComponent::new);
	private static final Supplier<AttachmentType<ProxyComponent<BoatComponent>>> BOAT = create("boat", BoatComponent::new);

	public static void init(IEventBus bus) {
		ComponentInitializer.getter = NeoForgeComponents::get;

		ATTACHMENT_TYPES.register(bus);
	}

	public static <T extends Component> T get(ResourceLocation id, Entity entity) {
		return (T) entity.getData(COMPONENTS.get(id)).component;
	}

	private static <T extends Component> Supplier<AttachmentType<ProxyComponent<T>>> create(String id, Supplier<T> supplier) {
		Supplier<AttachmentType<ProxyComponent<T>>> type = ATTACHMENT_TYPES.register(
			id,
			() -> AttachmentType.serializable(() -> new ProxyComponent<T>(supplier.get())).build()
		);
		COMPONENTS.put(WanderingWizardry.id(id), (Supplier<AttachmentType<ProxyComponent<?>>>) (Object) type);
		return type;
	}
}
