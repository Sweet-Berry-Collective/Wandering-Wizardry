package dev.sweetberry.wwizardry.client.content;

import com.terraformersmc.terraform.boat.api.client.TerraformBoatClientHelper;
import com.terraformersmc.terraform.sign.SpriteIdentifierRegistry;
import dev.sweetberry.wwizardry.Mod;
import dev.sweetberry.wwizardry.content.datagen.AbstractDataGenerator;
import dev.sweetberry.wwizardry.content.datagen.DatagenInitializer;
import dev.sweetberry.wwizardry.content.datagen.WoodType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Holder;

public class DatagenRegistryAttachment {
	public static void init() {
		DatagenInitializer.REGISTRY
			.holders()
			.map(Holder.Reference::value)
			.forEach(DatagenRegistryAttachment::checkGenerator);
	}

	public static void checkGenerator(AbstractDataGenerator dataGenerator) {
		if (dataGenerator instanceof WoodType woodType) {
			RenderLayers.put(RenderType.cutout(), woodType.DOOR, woodType.TRAPDOOR, woodType.SAPLING);
			SpriteIdentifierRegistry.INSTANCE.addIdentifier(new Material(Sheets.SIGN_SHEET, woodType.SIGN.getTexture()));
			SpriteIdentifierRegistry.INSTANCE.addIdentifier(new Material(Sheets.SIGN_SHEET, woodType.HANGING_SIGN.getTexture()));
			if (woodType.fungus)
				return;
			RenderLayers.put(RenderType.cutout(), woodType.LEAVES);
			TerraformBoatClientHelper.registerModelLayers(Mod.id(woodType.baseName), false);
		}
	}
}
