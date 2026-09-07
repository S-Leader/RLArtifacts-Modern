package artifacts.client.item.renderer;

import artifacts.client.item.model.NecklaceModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * Restores the crouching translation used by the original 1.12.2 amulet layer.
 */
public class NecklaceArtifactRenderer extends GenericArtifactRenderer {

    public NecklaceArtifactRenderer(String name, NecklaceModel model) {
        super(name, model);
    }

    @Override
    public void render(ItemStack stack, LivingEntity entity, int slotIndex, PoseStack poseStack,
                       MultiBufferSource multiBufferSource, int light, float limbSwing,
                       float limbSwingAmount, float partialTicks, float ageInTicks,
                       float netHeadYaw, float headPitch) {
        poseStack.pushPose();
        if (entity.isCrouching()) {
            poseStack.translate(0.0D, 0.2D, 0.0D);
        }
        super.render(stack, entity, slotIndex, poseStack, multiBufferSource, light, limbSwing,
                limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
        poseStack.popPose();
    }
}
