package artifacts.client.hallowstar;

import artifacts.Artifacts;
import artifacts.entity.HallowStarEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class HallowStarRenderer extends EntityRenderer<HallowStarEntity> {

    private static final ResourceLocation TEXTURE = Artifacts.id("textures/entity/hallow_star/hallow_star.png");
    private final HallowStarModel model;

    public HallowStarRenderer(EntityRendererProvider.Context context) {
        super(context);
        model = new HallowStarModel(context.bakeLayer(HallowStarModel.LAYER_LOCATION));
        shadowRadius = 0.3F;
    }

    @Override
    public void render(HallowStarEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        model.setupAnim(entity, 0.0F, 0.0F, entity.tickCount + partialTicks, 0.0F, 0.0F);
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        model.renderToBuffer(
                poseStack,
                consumer,
                LightTexture.pack(15, 15),
                OverlayTexture.NO_OVERLAY,
                1.0F,
                1.0F,
                1.0F,
                1.0F
        );
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(HallowStarEntity entity) {
        return TEXTURE;
    }
}
