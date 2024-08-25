package dev.sweetberry.wwizardry.content;

import dev.sweetberry.wwizardry.api.registry.RegistryCallback;
import dev.sweetberry.wwizardry.content.block.BlockInitializer;
import dev.sweetberry.wwizardry.content.component.ComponentInitializer;
import dev.sweetberry.wwizardry.content.criterion.CriterionInitializer;
import dev.sweetberry.wwizardry.content.datagen.DatagenInitializer;
import dev.sweetberry.wwizardry.content.entity.EntityInitializer;
import dev.sweetberry.wwizardry.content.events.EventInitializer;
import dev.sweetberry.wwizardry.content.item.ItemInitializer;
import dev.sweetberry.wwizardry.content.net.NetworkingInitializer;
import dev.sweetberry.wwizardry.content.recipe.RecipeInitializer;
import dev.sweetberry.wwizardry.content.sounds.SoundInitializer;
import dev.sweetberry.wwizardry.content.world.WorldgenInitializer;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public class ContentInitializer {
	public static void init() {
		ComponentInitializer.init();
		DatagenInitializer.init();
		WorldgenInitializer.init();
		NetworkingInitializer.init();
		EventInitializer.init();
	}

	public static void listenToAll(RegistryCallback<?> listener) {
		BlockInitializer.BLOCKS.listen((RegistryCallback<Block>) listener);
		BlockInitializer.BLOCK_ENTITIES.listen((RegistryCallback<BlockEntityType<?>>) listener);
		CriterionInitializer.CRITERION.listen((RegistryCallback<CriterionTrigger<?>>) listener);
		ItemInitializer.ITEMS.listen((RegistryCallback<Item>) listener);
		ItemInitializer.TABS.listen((RegistryCallback<CreativeModeTab>) listener);
		RecipeInitializer.RECIPE_SERIALIZERS.listen((RegistryCallback<RecipeSerializer<?>>) listener);
		RecipeInitializer.RECIPES.listen((RegistryCallback<RecipeType<?>>) listener);
		WorldgenInitializer.STRUCTURE_PROCESSORS.listen((RegistryCallback<StructureProcessorType<?>>) listener);
		SoundInitializer.SOUNDS.listen((RegistryCallback<SoundEvent>) listener);
		EntityInitializer.ENTITIES.listen((RegistryCallback<EntityType<?>>) listener);
	}
}
