package dev.sweetberry.wwizardry.client.render.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.client.render.animation.AnimationHelper;
import dev.sweetberry.wwizardry.client.render.animation.EasingFunction;
import dev.sweetberry.wwizardry.client.render.animation.Keyframe;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class AltarCatalyzerModel extends Model {
	public static final ResourceLocation ID = WanderingWizardry.id("altar_catalyzer");
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ID, "main");
    public static final ResourceLocation TEXTURE_ID = WanderingWizardry.id("textures/entity/altar.png");

	public static final Keyframe[] LAYER_1_FRAMES = new Keyframe[] {
		new Keyframe(EasingFunction::linear, 0, new Vec3(0, 0, 0), new Vec3(0, 0, 0)),
		new Keyframe(EasingFunction::linear, 0.75, new Vec3(0, 0, 0), new Vec3(0, 0, 0)),
		new Keyframe(EasingFunction::inOutBack, 1.5, new Vec3(0, 0, 0), new Vec3(0, -90, 0)),
		new Keyframe(EasingFunction::linear, 2.75, new Vec3(0, 0, 0), new Vec3(0, -90, 0)),
		new Keyframe(EasingFunction::inOutBack, 3.5, new Vec3(0, 0, 0), new Vec3(0, 0, 0)),
	};
	public static final Keyframe[] LAYER_2_FRAMES = new Keyframe[] {
		new Keyframe(EasingFunction::linear, 0, new Vec3(0, 0, 0), new Vec3(0, 0, 0)),
		new Keyframe(EasingFunction::linear, 4, new Vec3(0, 0, 0), new Vec3(0, -360, 0))
	};
	public static final Keyframe[] LAYER_2_FRAMES_FAST = new Keyframe[] {
		new Keyframe(EasingFunction::linear, 0, new Vec3(0, 0, 0), new Vec3(0, 0, 0)),
		new Keyframe(EasingFunction::linear, 4, new Vec3(0, 0, 0), new Vec3(0, -720, 0))
	};

	private final ModelPart layer1;
	private final ModelPart layer2;
	public float ticks = 0;
	public boolean crafting = false;
	public float timeToCraft = 0;

	public AltarCatalyzerModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        layer1 = root.getChild("layer1");
		layer2 = root.getChild("layer2");
	}

	public static LayerDefinition createLayer() {
		var meshdefinition = new MeshDefinition();
		var partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("layer1", CubeListBuilder.create().texOffs(-80, 80).addBox(-40.0F, 0.0F, -40.0F, 80.0F, 0.1F, 80.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		partdefinition.addOrReplaceChild("layer2", CubeListBuilder.create().texOffs(-80, 0).addBox(-40.0F, 0.0F, -40.0F, 80.0F, 0.1F, 80.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 80, 240);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		WanderingWizardry.id(timeToCraft + "");
		var seconds = ticks / 20;
		seconds %= 4;
		poseStack.pushPose();
		AnimationHelper.applyKeyframes(LAYER_1_FRAMES, seconds, poseStack);
		layer1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		poseStack.popPose();

		poseStack.pushPose();
		AnimationHelper.applyKeyframes(crafting ? LAYER_2_FRAMES_FAST : LAYER_2_FRAMES, seconds, poseStack);
		layer2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		poseStack.popPose();
	}
}
