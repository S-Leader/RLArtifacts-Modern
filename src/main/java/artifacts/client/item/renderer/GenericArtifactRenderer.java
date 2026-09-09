package artifacts.client.item.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.item.ItemStack;

public class GenericArtifactRenderer implements ArtifactRenderer {

    private final ResourceLocation texture;
    private final HumanoidModel<LivingEntity> model;

    public GenericArtifactRenderer(String name, HumanoidModel<LivingEntity> model) {
        this(ArtifactRenderer.getTexturePath(name), model);
    }

    public GenericArtifactRenderer(ResourceLocation texture, HumanoidModel<LivingEntity> model) {
        this.texture = texture;
        this.model = model;
    }

    protected ResourceLocation getTexture() {
        return texture;
    }

    protected HumanoidModel<LivingEntity> getModel() {
        return model;
    }

    @Override
    public void render(
            ItemStack stack,
            LivingEntity entity,
            int slotIndex,
            PoseStack poseStack,
            MultiBufferSource multiBufferSource,
            int light,
            float limbSwing,
            float limbSwingAmount,
            float partialTicks,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        poseStack.pushPose();
        HumanoidModel<LivingEntity> model = getModel();

        model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        model.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
        ArtifactRenderer.followBodyRotations(entity, model);

        if (entity instanceof Ghast) {
            model.head.yRot = model.body.yRot;
            // A ghast's body is a 16x16x16 cube spanning Y 9.6..25.6 in model
            // space. The cloak hood is 10x10x10 and spans Y -9.5..0.5, so
            // translating first and scaling by 16 / 10 makes it fit exactly.
            poseStack.translate(0.0F, 24.8F / 16.0F, 0.0F);
            poseStack.scale(1.6F, 1.6F, 1.6F);
        }

        render(poseStack, multiBufferSource, light, stack.hasFoil() && !(entity instanceof Ghast));
        poseStack.popPose();
    }

    protected void render(PoseStack matrixStack, MultiBufferSource buffer, int light, boolean hasFoil) {
        RenderType renderType = model.renderType(getTexture());
        VertexConsumer vertexBuilder = ItemRenderer.getFoilBuffer(buffer, renderType, false, hasFoil);
        model.renderToBuffer(matrixStack, vertexBuilder, light, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
    }
}
