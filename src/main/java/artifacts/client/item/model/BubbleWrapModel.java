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

/** Exact 1.12 Bubble Wrap box, kept separate from the modern charm-belt model. */
public class BubbleWrapModel extends HumanoidModel<LivingEntity> {

    public BubbleWrapModel(ModelPart root) {
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

    public static MeshDefinition createBubbleWrap() {
        MeshDefinition mesh = createMesh(CubeDeformation.NONE, 0.0F);
        mesh.getRoot().addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-5.0F, 3.0F, -3.0F, 10.0F, 8.0F, 6.0F),
                PartPose.ZERO
        );
        return mesh;
    }
}
