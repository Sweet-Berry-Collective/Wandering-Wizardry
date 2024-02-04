package dev.sweetberry.wwizardry.content;

import dev.sweetberry.wwizardry.api.registry.RegistryCallback;
import dev.sweetberry.wwizardry.content.block.BlockInitializer;
import dev.sweetberry.wwizardry.content.criterion.CriterionInitializer;
import dev.sweetberry.wwizardry.content.datagen.DatagenInitializer;
import dev.sweetberry.wwizardry.content.events.EventInitializer;
import dev.sweetberry.wwizardry.content.gamerule.GameruleInitializer;
import dev.sweetberry.wwizardry.content.item.ItemInitializer;
import dev.sweetberry.wwizardry.content.net.NetworkingInitializer;
import dev.sweetberry.wwizardry.content.painting.PaintingInitializer;
import dev.sweetberry.wwizardry.content.recipe.RecipeInitializer;
import dev.sweetberry.wwizardry.content.sounds.SoundInitializer;
import dev.sweetberry.wwizardry.content.trades.TradeInitializer;
import dev.sweetberry.wwizardry.content.world.WorldgenInitializer;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.Registry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public class ContentInitializer {
	public static void init() {
		listenToAll(Registry::register);

		BlockInitializer.init();
		ItemInitializer.init();
		DatagenInitializer.init();
		WorldgenInitializer.init();
		GameruleInitializer.init();
		NetworkingInitializer.init();
		TradeInitializer.init();
		EventInitializer.init();
	}

	public static void listenToAll(RegistryCallback<?> listener) {
		BlockInitializer.BLOCKS.listen((RegistryCallback<Block>) listener);
		BlockInitializer.BLOCK_ENTITIES.listen((RegistryCallback<BlockEntityType<?>>) listener);
		CriterionInitializer.CRITERION.listen((RegistryCallback<CriterionTrigger<?>>) listener);
		ItemInitializer.ITEMS.listen((RegistryCallback<Item>) listener);
		ItemInitializer.TABS.listen((RegistryCallback<CreativeModeTab>) listener);
		PaintingInitializer.PAINTINGS.listen((RegistryCallback<PaintingVariant>) listener);
		RecipeInitializer.RECIPE_SERIALIZERS.listen((RegistryCallback<RecipeSerializer<?>>) listener);
		WorldgenInitializer.STRUCTURE_PROCESSORS.listen((RegistryCallback<StructureProcessorType<?>>) listener);
		SoundInitializer.SOUNDS.listen((RegistryCallback<SoundEvent>) listener);
	}
}
