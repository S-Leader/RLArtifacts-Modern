package artifacts.client.item.renderer;

import artifacts.client.item.model.LegacyAmuletModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class LegacyAmuletArtifactRenderer extends GenericArtifactRenderer {

    public LegacyAmuletArtifactRenderer(String name, LegacyAmuletModel model) {
        super(name, model);
    }

    @Override
    public void render(ItemStack stack, LivingEntity entity, int slotIndex, PoseStack poseStack,
                       MultiBufferSource multiBufferSource, int light, float limbSwing,
                       float limbSwingAmount, float partialTicks, float ageInTicks,
                       float netHeadYaw, float headPitch) {
        poseStack.pushPose();
        poseStack.translate(0.0D, -0.02D, 0.0D);
        poseStack.scale(7.0F / 6.0F, 7.0F / 6.0F, 7.0F / 6.0F);
        super.render(stack, entity, slotIndex, poseStack, multiBufferSource, light, limbSwing,
                limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
        poseStack.popPose();
    }
}
