package dev.sweetberry.wwizardry.content.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.sweetberry.wwizardry.api.Lazy;
import dev.sweetberry.wwizardry.api.component.Component;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BoatComponent implements Component<BoatComponent> {
	public static Map<ResourceLocation, BoatType> BOATS = new HashMap<>();
	public static final Codec<BoatComponent> CODEC = RecordCodecBuilder.create(inst -> inst
		.group(
			ResourceLocation.CODEC.optionalFieldOf("id").forGetter(BoatComponent::type)
		).apply(inst, BoatComponent::new)
	);

	@Nullable
	public ResourceLocation type;

	public BoatComponent(Optional<ResourceLocation> type) {
		this.type = type.orElse(null);
	}

	public BoatComponent() {}

	public Optional<ResourceLocation> type() {
		return type == null ? Optional.empty() : Optional.of(type);
	}

	@Override
	public Codec<BoatComponent> codec() {
		return CODEC;
	}

	@Override
	public void copyFrom(BoatComponent other) {
		type = other.type;
	}

	public record BoatType(Lazy<Block> planks, Lazy<Item> boat, Lazy<Item> chest) {}
}
