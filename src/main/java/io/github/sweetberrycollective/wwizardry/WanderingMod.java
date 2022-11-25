package io.github.sweetberrycollective.wwizardry;

import io.github.sweetberrycollective.wwizardry.block.WanderingBlocks;
import io.github.sweetberrycollective.wwizardry.item.WanderingItems;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WanderingMod implements ModInitializer {
	public static final String MODID = "wwizardry";
	public static final Logger LOGGER = LoggerFactory.getLogger("Wandering Wizardry");

	@Override
	public void onInitialize(ModContainer mod) {
		WanderingBlocks.init();
		WanderingItems.init();
	}

	public static Identifier id(String path) {
		return new Identifier(MODID, path);
	}
}
