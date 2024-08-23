package dev.sweetberry.wwizardry.client;

import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.client.content.ClientContentInitializer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.Map;

public class WanderingWizardryClient {
	public static int tickCounter = 0;
	public static int useItemTick = -1;
	public static void init() {
		ClientContentInitializer.init();
	}

	public static ResourceLocation getBoatTextureLocation(ResourceLocation type, boolean chest) {
		var root = chest
			? "textures/entity/chest_boat/"
			: "textures/entity/boat/";

		return type.withPrefix(root).withSuffix(".png");
	}

	public static ModelLayerLocation getBoatLayerLocation(ResourceLocation id, boolean chest) {
		return new ModelLayerLocation(getBoatTextureLocation(id, chest), "main");
	}
}
