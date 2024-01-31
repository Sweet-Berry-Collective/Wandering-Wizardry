package dev.sweetberry.wwizardry.content;

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

public class ContentInitializer {
	public static void init() {
		BlockInitializer.init();
		ItemInitializer.init();
		PaintingInitializer.init();
		RecipeInitializer.init();
		DatagenInitializer.init();
		WorldgenInitializer.init();
		SoundInitializer.init();
		GameruleInitializer.init();
		NetworkingInitializer.init();
		TradeInitializer.init();
		EventInitializer.init();
		CriterionInitializer.init();
	}
}
