package dev.sweetberry.wwizardry.client.content;

import com.terraformersmc.terraform.boat.api.client.TerraformBoatClientHelper;
import com.terraformersmc.terraform.sign.SpriteIdentifierRegistry;
import dev.sweetberry.wwizardry.Mod;
import dev.sweetberry.wwizardry.content.datagen.AbstractDataGenerator;
import dev.sweetberry.wwizardry.content.datagen.DatagenInitializer;
import dev.sweetberry.wwizardry.content.datagen.WoodType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.resource.Material;
import net.minecraft.registry.Holder;

public class DatagenRegistryAttachment {
	public static void init() {
		DatagenInitializer.REGISTRY
			.holders()
			.map(Holder.Reference::value)
			.forEach(DatagenRegistryAttachment::checkGenerator);
	}

	public static void checkGenerator(AbstractDataGenerator dataGenerator) {
		if (dataGenerator instanceof WoodType woodType) {
			RenderLayers.put(RenderLayer.getCutout(), woodType.DOOR, woodType.TRAPDOOR, woodType.SAPLING);
			SpriteIdentifierRegistry.INSTANCE.addIdentifier(new Material(TexturedRenderLayers.SIGNS_ATLAS_TEXTURE, woodType.SIGN.getTexture()));
			SpriteIdentifierRegistry.INSTANCE.addIdentifier(new Material(TexturedRenderLayers.SIGNS_ATLAS_TEXTURE, woodType.HANGING_SIGN.getTexture()));
			if (woodType.fungus)
				return;
			RenderLayers.put(RenderLayer.getCutout(), woodType.LEAVES);
			TerraformBoatClientHelper.registerModelLayers(Mod.id(woodType.baseName), false);
		}
	}
}