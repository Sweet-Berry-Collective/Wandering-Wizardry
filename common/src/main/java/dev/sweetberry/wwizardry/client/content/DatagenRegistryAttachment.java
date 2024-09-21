package dev.sweetberry.wwizardry.client.content;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.client.WanderingWizardryClient;
import dev.sweetberry.wwizardry.content.datagen.AbstractDataGenerator;
import dev.sweetberry.wwizardry.content.datagen.DatagenInitializer;
import dev.sweetberry.wwizardry.content.datagen.WoodTypeGen;
import net.minecraft.client.renderer.RenderType;

public class DatagenRegistryAttachment {
	public static void init() {
		DatagenInitializer.REGISTRY
			.values()
			.forEach(DatagenRegistryAttachment::checkGenerator);
		AbstractDataGenerator.DATA_GENERATOR_CONSTRUCTED.listen(DatagenRegistryAttachment::checkGenerator);
	}

	public static void checkGenerator(AbstractDataGenerator dataGenerator) {
		if (dataGenerator instanceof WoodTypeGen woodTypeGen) {
			RenderLayers.put(RenderType.cutout(), woodTypeGen.DOOR, woodTypeGen.TRAPDOOR, woodTypeGen.SAPLING);
			if (woodTypeGen.fungus)
				return;
			RenderLayers.put(RenderType.cutout(), woodTypeGen.LEAVES);
		}
	}
}
