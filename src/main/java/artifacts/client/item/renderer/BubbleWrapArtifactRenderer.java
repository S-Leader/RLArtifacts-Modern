package artifacts.client.item.renderer;

import artifacts.client.item.model.BubbleWrapModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/** Applies the two scale operations used by RLArtifacts 1.12's ModelBubbleWrap. */
public class BubbleWrapArtifactRenderer extends GenericArtifactRenderer {

    public BubbleWrapArtifactRenderer(BubbleWrapModel model) {
        super("bubble_wrap", model);
    }

    @Override
    public void render(ItemStack stack, LivingEntity entity, int slotIndex, PoseStack poseStack,
                       MultiBufferSource multiBufferSource, int light, float limbSwing,
                       float limbSwingAmount, float partialTicks, float ageInTicks,
                       float netHeadYaw, float headPitch) {
        poseStack.pushPose();
        poseStack.scale(7.0F / 6.0F, 7.0F / 6.0F, 7.0F / 6.0F);
        poseStack.scale(1.0F, 1.0F, entity.getItemBySlot(EquipmentSlot.LEGS).isEmpty() ? 1.1F : 1.2F);
        super.render(stack, entity, slotIndex, poseStack, multiBufferSource, light, limbSwing,
                limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
        poseStack.popPose();
    }
}
