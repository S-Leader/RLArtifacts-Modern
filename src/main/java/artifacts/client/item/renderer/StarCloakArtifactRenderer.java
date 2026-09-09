package artifacts.client.item.renderer;

import artifacts.client.item.model.StarCloakModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.item.ItemStack;

/**
 * Keeps humanoid and ghast cloak pose state completely separate. Curio
 * renderers are shared, so using one model for both allowed a player's pose to
 * leak into the next ghast render.
 */
public class StarCloakArtifactRenderer implements ArtifactRenderer {

    private static final float GHAST_SCALE = 1.8F;
    private static final float GHAST_Y_OFFSET = 25.7F / 16.0F;

    private final ResourceLocation texture = ArtifactRenderer.getTexturePath("star_cloak", "star_cloak");
    private final ResourceLocation glowTexture = ArtifactRenderer.getTexturePath("star_cloak", "star_cloak_overlay");
    private final StarCloakModel humanoidModel;
    private final StarCloakModel ghastModel;

    public StarCloakArtifactRenderer(StarCloakModel humanoidModel, StarCloakModel ghastModel) {
        this.humanoidModel = humanoidModel;
        this.ghastModel = ghastModel;
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
        boolean isGhast = entity instanceof Ghast;
        StarCloakModel model = isGhast ? ghastModel : humanoidModel;

        if (isGhast) {
            resetGhastPose(model);
        }
        model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        model.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);

        if (isGhast) {
            // A ghast has no HumanoidModel for followBodyRotations to copy.
            // Keep its dedicated cloak model in a fixed, clean body pose.
            model.head.resetPose();
            model.body.resetPose();
            poseStack.pushPose();
            poseStack.translate(0.0F, GHAST_Y_OFFSET, 0.0F);
            poseStack.scale(GHAST_SCALE, GHAST_SCALE, GHAST_SCALE);
        } else {
            ArtifactRenderer.followBodyRotations(entity, model);
            poseStack.pushPose();
        }

        boolean hasFoil = stack.hasFoil() && !isGhast;
        renderModel(model, texture, poseStack, multiBufferSource, light, hasFoil);
        renderModel(model, glowTexture, poseStack, multiBufferSource, LightTexture.pack(15, 15), hasFoil);
        poseStack.popPose();
    }

    private static void resetGhastPose(StarCloakModel model) {
        model.head.resetPose();
        model.body.resetPose();
        model.crouching = false;
        model.riding = false;
        model.young = false;
        model.attackTime = 0.0F;
        model.swimAmount = 0.0F;
        model.leftArmPose = HumanoidModel.ArmPose.EMPTY;
        model.rightArmPose = HumanoidModel.ArmPose.EMPTY;
    }

    private static void renderModel(
            StarCloakModel model,
            ResourceLocation texture,
            PoseStack poseStack,
            MultiBufferSource multiBufferSource,
            int light,
            boolean hasFoil
    ) {
        RenderType renderType = model.renderType(texture);
        VertexConsumer vertexConsumer = ItemRenderer.getFoilBuffer(multiBufferSource, renderType, false, hasFoil);
        model.renderToBuffer(poseStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
    }
}
