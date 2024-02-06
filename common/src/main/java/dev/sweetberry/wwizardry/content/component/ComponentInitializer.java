package dev.sweetberry.wwizardry.content.component;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.api.component.Component;
import dev.sweetberry.wwizardry.content.datagen.DatagenInitializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class ComponentInitializer {
	public static ComponentGetter getter;

	public static ResourceLocation BOAT = WanderingWizardry.id("boat");
	public static ResourceLocation VOID_BAG = WanderingWizardry.id("void_bag");

	public static void init() {
		BoatComponent.BOATS.put(WanderingWizardry.id("denia"), new BoatComponent.BoatType(DatagenInitializer.DENIA_WOOD.PLANKS, DatagenInitializer.DENIA_WOOD.BOAT_ITEM, DatagenInitializer.DENIA_WOOD.BOAT_CHEST_ITEM));
	}

	public static <T extends Component> T getComponent(ResourceLocation id, Entity entity) {
		return getter.get(id, entity);
	}

	@FunctionalInterface
	public interface ComponentGetter {
		<T extends Component> T get(ResourceLocation id, Entity entity);
	}
}
