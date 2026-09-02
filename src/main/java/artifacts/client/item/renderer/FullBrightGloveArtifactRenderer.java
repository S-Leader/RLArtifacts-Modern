package artifacts.client.item.renderer;

import artifacts.client.item.model.ArmsModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.HumanoidArm;

import java.util.function.Function;

/**
 * Renders a glove texture at full brightness. The 1.12 Magma Stone used the
 * Fire Gauntlet's emissive arm layer without rendering the base glove.
 */
public class FullBrightGloveArtifactRenderer extends GloveArtifactRenderer {

    public FullBrightGloveArtifactRenderer(String wideTexture, String slimTexture, Function<Boolean, ArmsModel> model) {
        super(wideTexture, slimTexture, model);
    }

    @Override
    protected void renderArm(ArmsModel model, PoseStack poseStack, MultiBufferSource multiBufferSource, HumanoidArm armSide, int light, boolean hasSlimArms, boolean hasFoil) {
        super.renderArm(model, poseStack, multiBufferSource, armSide, LightTexture.pack(15, 15), hasSlimArms, hasFoil);
    }

    @Override
    protected void renderFirstPersonArm(ArmsModel model, ModelPart arm, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, boolean hasSlimArms, boolean hasFoil) {
        super.renderFirstPersonArm(model, arm, poseStack, multiBufferSource, LightTexture.pack(15, 15), hasSlimArms, hasFoil);
    }
}
