package artifacts.client.item.model;

import artifacts.registry.ModItems;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.LivingEntity;

public class StarCloakModel extends HumanoidModel<LivingEntity> {

    private final ModelPart hoodUp;
    private final ModelPart hoodDown;

    public StarCloakModel(ModelPart root) {
        super(root, RenderType::entityCutoutNoCull);
        hoodUp = head.getChild("hood_up");
        hoodDown = body.getChild("hood_down");
    }

    @Override
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of(head);
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(body);
    }

    @Override
    public void setupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        // The 1.12 layer rendered the hood up only when the head was uncovered.
        boolean renderHoodUp = entity.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.HEAD).isEmpty()
                && !ModItems.PLASTIC_DRINKING_HAT.get().isEquippedBy(entity);
        hoodUp.visible = renderHoodUp;
        hoodDown.visible = !renderHoodUp;
    }

    public static MeshDefinition createStarCloak() {
        MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
        PartDefinition root = mesh.getRoot();

        // HumanoidModel.createMesh includes full player body cubes. Rendering
        // those cubes with the cloak texture creates the blue sheet in front of
        // the player, so replace the two rendered base parts with empty pivots.
        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);

        // Keep the original 1.12 hierarchy and pivots exactly. Flattening these
        // pieces changes both the collar position and the three cloak panels.
        PartDefinition cloak = body.addOrReplaceChild(
                "cloak",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(0.0F, 0.4F, 2.96F, 0.0436F, 0.0F, 0.0F)
        );
        cloak.addOrReplaceChild(
                "neck",
                CubeListBuilder.create().texOffs(32, 6).addBox(-5.5F, -24.8F, 2.5F, 11.0F, 4.0F, 5.0F),
                PartPose.offsetAndRotation(0.0F, 23.6F, -8.96F, -0.0436F, 0.0F, 0.0F)
        );
        cloak.addOrReplaceChild(
                "cloak_left",
                CubeListBuilder.create().texOffs(14, 0).addBox(-6.6F, -9.6F, -0.38F, 6.0F, 20.0F, 1.0F),
                PartPose.offsetAndRotation(0.0F, 10.0F, -0.18F, 0.0F, -0.0436F, 0.1309F)
        );
        cloak.addOrReplaceChild(
                "cloak_right",
                CubeListBuilder.create().texOffs(13, 21).addBox(-3.0F, -10.0F, -0.5F, 6.0F, 20.0F, 1.0F),
                PartPose.offsetAndRotation(3.6F, 9.9F, -0.06F, 0.0F, 0.0436F, -0.1309F)
        );
        cloak.addOrReplaceChild(
                "cloak_center",
                CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, 0.0F, -1.0F, 5.0F, 20.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 0.1F, -0.31F, 0.0436F, 0.0F, 0.0F)
        );
        body.addOrReplaceChild(
                "hood_down",
                CubeListBuilder.create().texOffs(38, 0).addBox(-5.0F, -24.5F, 3.0F, 10.0F, 3.0F, 3.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F)
        );
        head.addOrReplaceChild(
                "hood_up",
                CubeListBuilder.create().texOffs(0, 44).addBox(-5.0F, -33.5F, -5.0F, 10.0F, 10.0F, 10.0F),
                PartPose.offset(0.0F, 24.0F, 0.0F)
        );
        return mesh;
    }
}
