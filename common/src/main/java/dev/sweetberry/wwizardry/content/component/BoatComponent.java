package dev.sweetberry.wwizardry.content.component;

import dev.sweetberry.wwizardry.api.component.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class BoatComponent implements Component {
	public static Map<ResourceLocation, BoatType> BOATS = new HashMap<>();

	public Boat boat;
	@Nullable
	public ResourceLocation type;

	public BoatComponent(Boat boat) {
		this.boat = boat;
	}

	@Override
	public void fromNbt(CompoundTag tag) {
		type = tag.contains("id")
			? new ResourceLocation(tag.getString("id"))
			: null;
	}

	@Override
	public void toNbt(CompoundTag tag) {
		if (type != null)
			tag.putString("id", type.toString());
	}

	public record BoatType(Block planks, Item boat, Item chest) {}
}
