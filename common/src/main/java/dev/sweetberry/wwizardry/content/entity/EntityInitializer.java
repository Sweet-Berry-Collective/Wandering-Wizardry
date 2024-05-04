package dev.sweetberry.wwizardry.content.entity;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.api.Lazy;
import dev.sweetberry.wwizardry.api.registry.RegistryContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class EntityInitializer {
	public static final RegistryContext<EntityType<?>> ENTITIES = new RegistryContext<>(BuiltInRegistries.ENTITY_TYPE);
	public static final List<AttributeSupplierData> SUPPLIER_DATA = new ArrayList<>();

	public static final Lazy<EntityType<Snail>> SNAIL = registerEntity(
		"snail",
		() -> EntityType.Builder
			.of(Snail::new, MobCategory.CREATURE)
			.sized(0.4f, 0.4f)
			.passengerAttachments(new Vec3(0, 0.3125f, -0.0625f))
			.eyeHeight(0.125f)
			.clientTrackingRange(10)
			.build("wwizardry:snail"),
		Snail::createAttributes
	);

	public static void init() {}

	public static <T extends Entity> Lazy<EntityType<T>> registerEntity(String id, Supplier<EntityType<T>> entity, Supplier<AttributeSupplier> supplier) {
		var value = EntityInitializer.<T>entities().register(WanderingWizardry.id(id), entity);
		SUPPLIER_DATA.add(new AttributeSupplierData((Supplier<EntityType<?>>) (Object) value, supplier.get()));
		return value;
	}

	public static <T extends Entity> RegistryContext<EntityType<T>> entities() {
		return (RegistryContext<EntityType<T>>) (Object) ENTITIES;
	}

	public record AttributeSupplierData(Supplier<EntityType<?>> entity, AttributeSupplier supplier) {}
}
