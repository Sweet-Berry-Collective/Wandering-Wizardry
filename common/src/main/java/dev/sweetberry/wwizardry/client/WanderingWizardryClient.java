package dev.sweetberry.wwizardry.client;

import dev.sweetberry.wwizardry.WanderingWizardry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.Map;

public class WanderingWizardryClient {
	public static final Map<ResourceLocation, Material> SIGN_MATERIALS = new HashMap<>();
	public static final Map<ResourceLocation, Material> HANGING_SIGN_MATERIALS = new HashMap<>();

	public static int tickCounter = 0;
	public static int useItemTick = -1;
	public static void init() {}

	public static ResourceLocation getBoatTextureLocation(ResourceLocation type, boolean chest) {
		var root = chest
			? "textures/entity/chest_boat/"
			: "textures/entity/boat/";

		return type.withPrefix(root).withSuffix(".png");
	}

	public static ModelLayerLocation getBoatLayerLocation(ResourceLocation id, boolean chest) {
		return new ModelLayerLocation(getBoatTextureLocation(id, chest), "main");
	}

	public static ResourceLocation getSignTextureLocation(ResourceLocation type, boolean hanging) {
		return type.withPrefix("entity/signs/" + (hanging ? "hanging/" : ""));
	}

	public static ModelLayerLocation getSignLayerLocation(ResourceLocation id, boolean hanging) {
		return new ModelLayerLocation(getSignTextureLocation(id, hanging), "main");
	}

	public static void addSignMaterial(ResourceLocation id) {
		SIGN_MATERIALS.put(id, new Material(Sheets.SIGN_SHEET, getSignTextureLocation(id, false)));
		HANGING_SIGN_MATERIALS.put(id, new Material(Sheets.SIGN_SHEET, getSignTextureLocation(id, true)));
	}
}
