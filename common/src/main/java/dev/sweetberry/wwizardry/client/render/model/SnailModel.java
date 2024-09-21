package dev.sweetberry.wwizardry.client.render.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.sweetberry.wwizardry.WanderingWizardry;
import dev.sweetberry.wwizardry.content.entity.Snail;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class SnailModel extends EntityModel<Snail> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(WanderingWizardry.id("snail"), "main");
	private final ModelPart body;
	private final ModelPart leftEye;
	private final ModelPart rightEye;
	private final ModelPart shell;

	public SnailModel(ModelPart root) {
		this.body = root.getChild("Body");
		this.leftEye = root.getChild("EyeL");
		this.rightEye = root.getChild("EyeR");
		this.shell = root.getChild("Shell");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.0F, -3.5F, 3.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition leftEye = partdefinition.addOrReplaceChild("EyeL", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(1.0F, 23.25F, -3.0F, 0.0873F, 0.0F, 0.0873F));

		PartDefinition rightEye = partdefinition.addOrReplaceChild("EyeR", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(-1.0F, 23.25F, -3.0F, 0.0873F, 0.0F, -0.0873F));

		PartDefinition shell = partdefinition.addOrReplaceChild("Shell", CubeListBuilder.create().texOffs(0, 8).addBox(-1.5F, -2.0F, -2.0F, 3.0F, 4.0F, 4.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, 21.25F, 1.0F, -0.0873F, 0.0F, 0.0F));

		PartDefinition shroom = shell.addOrReplaceChild("shroom", CubeListBuilder.create().texOffs(13, 4).addBox(-2.0F, -1.5F, 0.5F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
			.texOffs(13, 1).addBox(-0.5F, -1.5F, -1.0F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.25F, -3.25F, 1.25F, -0.1309F, -0.7854F, -0.0436F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(Snail entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		leftEye.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		rightEye.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		shell.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}
}
