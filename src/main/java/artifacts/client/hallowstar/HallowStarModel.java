package artifacts.client.hallowstar;

import artifacts.Artifacts;
import artifacts.entity.HallowStarEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;

public class HallowStarModel extends EntityModel<HallowStarEntity> {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Artifacts.id("hallow_star"), "main");

    private final ModelPart starA;
    private final ModelPart starB;

    public HallowStarModel(ModelPart root) {
        starA = root.getChild("star_a");
        starB = root.getChild("star_b");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        mesh.getRoot().addOrReplaceChild(
                "star_a",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -4.0F, -4.0F, 2.0F, 8.0F, 8.0F),
                PartPose.rotation((float) Math.PI / 4.0F, 0.0F, 0.0F)
        );
        mesh.getRoot().addOrReplaceChild(
                "star_b",
                CubeListBuilder.create().texOffs(0, 16).addBox(-0.5F, -4.0F, -4.0F, 2.0F, 8.0F, 8.0F),
                PartPose.ZERO
        );
        return LayerDefinition.create(mesh, 32, 32);
    }

    @Override
    public void setupAnim(HallowStarEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float yRotation = ageInTicks * ((float) Math.PI / 6.0F);
        float zRotation = ageInTicks * ((float) Math.PI / 15.0F);
        starA.yRot = yRotation;
        starB.yRot = yRotation;
        starA.zRot = zRotation;
        starB.zRot = zRotation;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        starA.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        starB.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
