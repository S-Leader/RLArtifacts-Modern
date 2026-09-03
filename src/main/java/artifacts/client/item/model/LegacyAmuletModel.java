package artifacts.client.item.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.LivingEntity;

/**
 * Port of RLArtifacts 1.12's ModelAmulet geometry.
 */
public class LegacyAmuletModel extends HumanoidModel<LivingEntity> {

    public LegacyAmuletModel(ModelPart root) {
        super(root, RenderType::entityTranslucent);
    }

    @Override
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of();
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(body);
    }

    public static MeshDefinition createAmulet() {
        MeshDefinition mesh = createMesh(CubeDeformation.NONE, 0.0F);
        mesh.getRoot().addOrReplaceChild(
                "body",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F)
                        // The original model applied a 0.5 Z scale only to this gem.
                        .texOffs(24, 0).addBox(-0.5F, 3.0F, -2.25F, 1.0F, 1.0F, 0.5F),
                PartPose.ZERO
        );
        return mesh;
    }
}
